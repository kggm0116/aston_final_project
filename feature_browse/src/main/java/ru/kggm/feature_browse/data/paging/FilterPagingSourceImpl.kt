package ru.kggm.feature_browse.data.paging

import android.util.Log
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.kggm.feature_browse.domain.paging.FilterPagingSource
import kotlin.math.max

abstract class FilterPagingSourceImpl<TData : Any, TFilters : Any, TResponse : Any, TOut : Any>(
    filterParameters: TFilters
) : FilterPagingSource<TOut, TFilters>(filterParameters) {

    companion object {
        private const val STARTING_KEY = 0
        private const val SIMULATE_DELAY = true
    }

    open val logTag = "BasePagingSourceImpl"

    abstract val itemsPerNetworkPage: Int

    abstract suspend fun onClearCache()
    abstract fun getNetworkConstraints(response: TResponse): NetworkConstants
    abstract suspend fun fetchFromDatabase(range: IntRange, filters: TFilters): List<TData>
    abstract suspend fun makeNetworkCall(pageNumber: Int): TResponse
    abstract suspend fun cacheItems(items: List<TData>)
    abstract fun mapData(item: TData): TOut
    abstract fun mapResponse(response: TResponse): List<TData>

    data class NetworkConstants(val pageCount: Int, val itemCount: Int)

    private var networkConstants = NetworkConstants(Int.MAX_VALUE, Int.MAX_VALUE)

    final override fun getRefreshKey(state: PagingState<Int, TOut>) = 0

    final override suspend fun clearCache() {
        withContext(Dispatchers.IO) {
            onClearCache()
        }
    }

    final override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TOut> =
        withContext(Dispatchers.IO) {
            val firstItem = params.key ?: STARTING_KEY
            val itemRange = firstItem until firstItem + params.loadSize
            Log.i(logTag, "Loading items ${itemRange.first}..${itemRange.last}")

            val itemsFromDatabase = fetchFromDatabase(itemRange, filters)
            val firstFetchedItem = itemRange.first + itemsFromDatabase.size

            var networkError = false
            val allItemsFromNetwork =
                if (itemsFromDatabase.size < itemRange.last - itemRange.first + 1) {
                    val fetchRange = firstFetchedItem until firstFetchedItem + params.loadSize
                    val firstPage = fetchRange.first / itemsPerNetworkPage + 1
                    val lastPage =
                        ((fetchRange.first + params.loadSize) / itemsPerNetworkPage + 1)
                            .coerceAtMost(networkConstants.pageCount)
                    try {
                        if (SIMULATE_DELAY) delay(500)
                        fetchFromNetwork(firstPage..lastPage)
                    } catch (exception: Throwable) {
                        networkError = true
                        emptyList()
                    }
                } else emptyList()

            val itemsFromNetwork = allItemsFromNetwork
                .drop(firstItem % itemsPerNetworkPage)
                .take(params.loadSize)
            cacheItems(itemsFromNetwork)

            val combinedItems = itemsFromDatabase + itemsFromNetwork

            if (combinedItems.isEmpty() && networkError)
                return@withContext LoadResult.Error(Error("No network access and no cached items"))

            return@withContext if (combinedItems.isEmpty()) {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            } else {
                LoadResult.Page(
                    data = combinedItems.map { mapData(it) },
                    prevKey = when (itemRange.first) {
                        STARTING_KEY -> null
                        else -> when (val prevKey =
                            max(STARTING_KEY, itemRange.first - params.loadSize)) {
                            STARTING_KEY -> null
                            else -> prevKey
                        }
                    },
                    nextKey = (itemRange.last + 1)
                        .takeIf { it < (networkConstants.itemCount) }
                )
            }
        }

    private suspend fun fetchFromNetwork(
        range: IntRange
    ): List<TData> {
        val fetchedItems = mutableListOf<TData>()
        for (iPage in range) {
            if (iPage > networkConstants.pageCount)
                break
            makeNetworkCall(iPage)
                .also { getNetworkConstraints(it) }
                .let { response ->
                    mapResponse(response).let { items ->
                        fetchedItems.addAll(items)
                    }
                }
        }
        return fetchedItems
    }
}