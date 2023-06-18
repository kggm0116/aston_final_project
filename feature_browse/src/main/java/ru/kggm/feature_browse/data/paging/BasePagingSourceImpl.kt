package ru.kggm.feature_browse.data.paging

import android.util.Log
import androidx.paging.PagingState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.kggm.core.utility.classTagOf
import ru.kggm.feature_browse.data.database.daos.CharacterDao
import ru.kggm.feature_browse.data.entities.CharacterDataEntity
import ru.kggm.feature_browse.data.entities.CharacterDataEntity.Companion.toDomainEntity
import ru.kggm.feature_browse.data.network.dtos.CharacterPageResponse
import ru.kggm.feature_browse.data.network.services.CharacterService
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.entities.CharacterFilterParameters
import ru.kggm.feature_browse.domain.paging.BasePagingSource
import ru.kggm.feature_browse.domain.paging.CharacterPagingSource
import javax.inject.Inject
import kotlin.math.max

abstract class BasePagingSourceImpl<TData : Any, TOut : Any>(
    filterParameters: CharacterFilterParameters,
    private val characterService: CharacterService,
    private val characterDao: CharacterDao,
) : BasePagingSource<TOut>(filterParameters) {

    abstract fun getItemsPerNetworkPage(): Int

    companion object {
        private const val STARTING_KEY = 0
        private const val SIMULATE_DELAY = true
    }

    abstract suspend fun onClearCache()

    override suspend fun clearCache() {
        withContext(Dispatchers.IO) {
            onClearCache()
        }
    }

    private data class NetworkConstants(val pageCount: Int, val entityCount: Int)

    override fun getRefreshKey(state: PagingState<Int, TOut>) = 0

    private var networkConstants = NetworkConstants(Int.MAX_VALUE, Int.MAX_VALUE)
    private fun setNetworkConstants(response: CharacterPageResponse) {
        networkConstants = NetworkConstants(
            pageCount = response.info.pageCount,
            entityCount = response.info.characterCount
        )
    }

    //

//    companion object {
//        private const val NETWORK_ITEMS_PER_PAGE = 20
//        private const val STARTING_KEY = 0
//        private val tag = classTagOf<BasePagingSourceImpl>()
//        private const val SIMULATE_DELAY = true
//    }

//    override suspend fun clearCache() {
//        withContext(Dispatchers.IO) {
//            characterDao.deleteAll()
//            Log.i(tag, "Cleared cache")
//        }
//    }

//    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>) = 0



    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> =
        withContext(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e(tag, throwable.stackTraceToString())
        }) {
            val firstItem = params.key ?: STARTING_KEY
            val itemRange = firstItem until firstItem + params.loadSize
            Log.i(tag, "Loading items ${itemRange.first}..${itemRange.last}")

            val itemsFromDatabase = fetchFromDatabase(itemRange)
            val firstFetchedItem = itemRange.first + itemsFromDatabase.size

            var networkError = false
            val allItemsFromNetwork = if (itemsFromDatabase.size < itemRange.last - itemRange.first + 1) {
                val fetchRange = firstFetchedItem until firstFetchedItem + params.loadSize
                val firstPage = fetchRange.first / NETWORK_ITEMS_PER_PAGE + 1
                val lastPage = ((fetchRange.first + params.loadSize) / NETWORK_ITEMS_PER_PAGE + 1)
                    .coerceAtMost(networkConstants.pageCount)
                try {
                    if (SIMULATE_DELAY) delay(500)
                    fetchFromNetwork(firstPage..lastPage)
                }
                catch (exception: Throwable) {
                    networkError = true
                    emptyList()
                }
            } else emptyList()

            val itemsFromNetwork = allItemsFromNetwork
                .drop(firstItem % NETWORK_ITEMS_PER_PAGE)
                .take(params.loadSize)
            characterDao.insertOrUpdate(itemsFromNetwork)

            val combinedItems = itemsFromDatabase + itemsFromNetwork

            if (combinedItems.isEmpty() && networkError)
                return@withContext LoadResult.Error(Error("No network access and no cached items"))

            return@withContext LoadResult.Page(
                data = combinedItems
                    .map { it.toDomainEntity() },
                prevKey = if (combinedItems.isEmpty()) {
                    null
                } else {
                    when (itemRange.first) {
                        STARTING_KEY -> null
                        else -> when (val prevKey =
                            max(STARTING_KEY, itemRange.first - params.loadSize)) {
                            STARTING_KEY -> null
                            else -> prevKey
                        }
                    }
                },
                nextKey = if (combinedItems.isEmpty()) {
                    null
                } else {
                    (itemRange.last + 1)
                        .takeIf { it < (networkConstants.characterCount) }
                }
            )
        }

    private suspend fun fetchFromDatabase(range: IntRange) = with(filterParameters) {
        characterDao.getRangeFiltered(
            skip = range.first,
            take = range.last - range.first + 1,
            name = name,
            status = status,
            type = type,
            species = species,
            gender = gender
        )
            .first()
    }

    private suspend fun fetchFromNetwork(pages: IntRange): List<CharacterDataEntity> {
        val fetchedItems = mutableListOf<CharacterDataEntity>()
        for (iPage in pages) {
            if (iPage > networkConstants.pageCount)
                break
            makeNetworkCall(iPage)
                .also { setNetworkConstants(it) }
                .results
                .map { it.toDataEntity() }
                .let {
                    fetchedItems.addAll(it)
                }
        }
        return fetchedItems
    }

    private suspend fun makeNetworkCall(pageNumber: Int) = with(filterParameters) {
        characterService.getCharacterPage(
            pageNumber = pageNumber,
            name = name,
            status = status,
            type = type,
            species = species,
            gender = gender
        )
    }
}