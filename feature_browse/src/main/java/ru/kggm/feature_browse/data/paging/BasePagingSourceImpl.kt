package ru.kggm.feature_browse.data.paging

import android.util.Log
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.kggm.feature_browse.domain.paging.FilterPagingSource
import kotlin.math.max

abstract class BasePagingSourceImpl<TData, TFilters, TOut>(
    filterParameters: TFilters
) : FilterPagingSource<TOut, TFilters>(filterParameters)
        where TData : Any,
              TFilters : Any,
              TOut : Any {

    companion object {
        private const val STARTING_KEY = 0
        private const val SIMULATED_DELAY_MS = 500L
    }

    protected open val logTag = "BasePagingSourceImpl"

    protected abstract suspend fun fetchFromNetwork(itemRange: IntRange): List<TData>
    protected abstract suspend fun fetchFromDatabase(range: IntRange): List<TData>

    protected abstract suspend fun cacheItems(items: List<TData>)
    abstract suspend fun onClearCache()

    abstract val itemComparator: Comparator<TData>
    abstract fun mapData(item: TData): TOut

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
                fetchFromNetwork(itemRange)
            } catch (httpException: HttpException) {
                Log.i(logTag, "Network error: ${httpException.message}")
                networkCallSuccessful = false
                emptyList()
            } catch (throwable: Throwable) {
                Log.e(
                    logTag,
                    "Unexpected error during network call:\n${throwable.stackTraceToString()}"
                )
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
                .sortedWith(itemComparator)
            Log.i(
                logTag,
                "Loaded ${itemsFromDatabase.size} from cache, ${itemsFromNetwork.size} from network"
            )

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
                    nextKey = (itemRange.last + 1).takeIf { networkCallSuccessful }
                )
            }
        }
}