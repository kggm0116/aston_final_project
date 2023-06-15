package ru.kggm.feature_browse.data.paging

import android.util.Log
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.data.database.daos.CharacterDao
import ru.kggm.feature_browse.data.entities.CharacterDataEntity
import ru.kggm.feature_browse.data.entities.CharacterDataEntity.Companion.toDomainEntity
import ru.kggm.feature_browse.data.network.dtos.CharacterPageResponse
import ru.kggm.feature_browse.data.network.services.CharacterService
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.entities.CharacterFilter
import ru.kggm.feature_browse.domain.entities.CharacterPagingSource
import javax.inject.Inject
import kotlin.math.max

class CharacterPagingSourceImpl @Inject constructor(
    private val characterService: CharacterService,
    private val characterDao: CharacterDao
) : CharacterPagingSource() {

    companion object {
        private const val NETWORK_ITEMS_PER_PAGE = 20
        private const val STARTING_KEY = 0
        private val tag = classTag()
    }

    init {
        Log.i(classTag(), "Initialized")
    }

    override suspend fun invalidateCache() {
        withContext(Dispatchers.IO) {
            characterDao.deleteAll()
            Log.i(tag, "Cleared cache")
        }
    }

    override suspend fun setFilters(search: String, filters: List<CharacterFilter>) {
        require(filters.groupBy)
        filters.
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>) = 0

    private data class NetworkConstants(val pageCount: Int, val characterCount: Int)

    private var networkConstants: NetworkConstants? = null
    private fun trySetNetworkConstants(response: CharacterPageResponse) {
        if (networkConstants == null) {
            networkConstants = NetworkConstants(
                pageCount = response.info.pageCount,
                characterCount = response.info.characterCount
            )
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> =
        withContext(Dispatchers.IO) {
            val firstItem = params.key ?: STARTING_KEY
            val itemRange = firstItem until firstItem + params.loadSize
            Log.i(tag, "Loading items ${itemRange.first}..${itemRange.last}")

            val itemsFromDatabase = fetchFromDatabase(itemRange)
            val firstFetchedItem = itemRange.first + itemsFromDatabase.size

            val fetchedItems = if (itemsFromDatabase.size < itemRange.last - itemRange.first + 1) {
                val fetchRange = firstFetchedItem until firstFetchedItem + params.loadSize
                val firstPage = fetchRange.first / NETWORK_ITEMS_PER_PAGE + 1
                val lastPage = ((fetchRange.first + params.loadSize) / NETWORK_ITEMS_PER_PAGE + 1)
                    .coerceAtMost(networkConstants?.pageCount ?: Int.MAX_VALUE)

                try {
                    fetchFromNetwork(firstPage..lastPage)
                } catch (e: Throwable) {
                    return@withContext LoadResult.Error(e)
                }
            } else emptyList()

            val itemsFromNetwork = fetchedItems
                .drop(firstItem % NETWORK_ITEMS_PER_PAGE)
                .take(params.loadSize)
            characterDao.add(itemsFromNetwork)

            Log.i(tag, "Loaded ${itemsFromDatabase.size} from database, ${itemsFromNetwork.size} from network")

            return@withContext LoadResult.Page(
                data = (itemsFromDatabase + itemsFromNetwork)
                    .map { it.toDomainEntity() },
                prevKey = when (itemRange.first) {
                    STARTING_KEY -> null
                    else -> when (val prevKey =
                        max(STARTING_KEY, itemRange.first - params.loadSize)) {
                        STARTING_KEY -> null
                        else -> prevKey
                    }
                },
                nextKey = (itemRange.last + 1)
                    .takeIf { it < (networkConstants?.characterCount ?: Int.MAX_VALUE) }
            )
        }

    private suspend fun fetchFromDatabase(range: IntRange): List<CharacterDataEntity> {
        return characterDao.getRange(range.first, range.last - range.first + 1).first()
    }

    private fun fetchFromNetwork(pages: IntRange): List<CharacterDataEntity> {
        val fetchedItems = mutableListOf<CharacterDataEntity>()
        for (iPage in pages) {
            characterService.getCharacterPage(iPage).join()
                .also { trySetNetworkConstants(it) }
                .results
                .map { it.toDataEntity() }
                .let { fetchedItems.addAll(it) }
        }
        return fetchedItems
    }
}