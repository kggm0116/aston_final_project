package ru.kggm.feature_browse.data.paging

import android.util.Log
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.kggm.feature_browse.domain.paging.FilterPagingSource
import kotlin.math.max

abstract class FilterPagingSourceImpl<TData : Any, TFilters : Any, TPage : Any, TOut : Any>(
    filterParameters: TFilters
) : FilterPagingSource<TOut, TFilters>(filterParameters) {

    companion object {
        private const val STARTING_KEY = 0
        private const val SIMULATED_DELAY_MS = 500L
    }

    open val logTag = "BasePagingSourceImpl"

    abstract val itemsPerNetworkPage: Int

    abstract suspend fun onClearCache()
    abstract fun getNetworkConstraints(response: TPage): NetworkConstants
    abstract suspend fun fetchFromDatabase(range: IntRange): List<TData>
    abstract suspend fun fetchNetworkPage(pageNumber: Int): TPage
    abstract suspend fun cacheItems(items: List<TData>)
    abstract val itemsSortComparator: Comparator<TData>
    abstract fun mapData(item: TData): TOut
    abstract fun mapNetworkPage(page: TPage): List<TData>

    data class NetworkConstants(val pageCount: Int, val itemCount: Int)

    private var networkConstants = NetworkConstants(Int.MAX_VALUE, Int.MAX_VALUE)

    final override fun getRefreshKey(state: PagingState<Int, TOut>) = 0

    final override suspend fun clearCache() {
        Log.i(logTag, "Clearing cache")
        withContext(Dispatchers.IO) {
            onClearCache()
        }
    }

    final override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TOut> =
        withContext(Dispatchers.IO) {
            val firstItem = params.key ?: STARTING_KEY
            val itemRange = firstItem until firstItem + params.loadSize
            Log.i(logTag, "Loading items ${itemRange.first}..${itemRange.last}")

            var networkCallSuccessful = true
            val itemsFromNetwork = try {
                delay(SIMULATED_DELAY_MS)
                fetchItemsFromNetwork(itemRange)
            } catch (throwable: Throwable) {
                Log.i(logTag, "Network error: ${throwable.message}")
                networkCallSuccessful = false
                emptyList()
            }

            val itemsFromDatabase = if (!networkCallSuccessful) {
                fetchFromDatabase(itemRange)
            } else {
                emptyList()
            }

            cacheItems(itemsFromNetwork)

            val combinedItems = (itemsFromDatabase + itemsFromNetwork)
                .sortedWith(itemsSortComparator)
            Log.i(logTag, "Loaded ${itemsFromDatabase.size} from cache, ${itemsFromNetwork.size} from network")

            if (combinedItems.isEmpty() && !networkCallSuccessful)
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

    private suspend fun fetchItemsFromNetwork(
        itemRange: IntRange
    ): List<TData> {
        val fetchedItems = mutableListOf<TData>()
        val itemCount = itemRange.last - itemRange.first + 1
        var iPage = itemRange.first / itemsPerNetworkPage + 1
        while (fetchedItems.size < itemCount) {
            if (iPage >= networkConstants.pageCount)
                break
            fetchNetworkPage(iPage++)
                .also { networkConstants = getNetworkConstraints(it) }
                .let { response ->
                    mapNetworkPage(response).let { items ->
                        fetchedItems.addAll(items)
                    }
                }
        }
        return fetchedItems.take(itemCount)
    }
}