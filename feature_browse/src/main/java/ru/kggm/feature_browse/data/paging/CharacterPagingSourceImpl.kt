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
import ru.kggm.feature_browse.domain.entities.CharacterPagingSource
import java.util.concurrent.CompletionException
import javax.inject.Inject
import kotlin.math.max

class CharacterPagingSourceImpl @Inject constructor(
    filterParameters: CharacterFilterParameters,
    private val characterService: CharacterService,
    private val characterDao: CharacterDao,
) : CharacterPagingSource(filterParameters) {

    companion object {
        private const val NETWORK_ITEMS_PER_PAGE = 20
        private const val STARTING_KEY = 0
        private val tag = classTagOf<CharacterPagingSourceImpl>()
        private const val SIMULATE_DELAY = true
    }

    init {
        Log.i(tag, "Initialized with filter params: $filterParameters")
    }

    override suspend fun invalidateAndClearCache() {
        withContext(Dispatchers.IO) {
            characterDao.deleteAll()
            Log.i(tag, "Cleared cache")
        }
        invalidate()
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>) = 0

    private data class NetworkConstants(val pageCount: Int, val characterCount: Int)

    private var networkConstants = NetworkConstants(Int.MAX_VALUE, Int.MAX_VALUE)
    private fun setNetworkConstants(response: CharacterPageResponse) {
        networkConstants = NetworkConstants(
            pageCount = response.info.pageCount,
            characterCount = response.info.characterCount
        )
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> =
        withContext(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e(tag, throwable.stackTraceToString())
        }) {
            val firstItem = params.key ?: STARTING_KEY
            val itemRange = firstItem until firstItem + params.loadSize
            Log.i(tag, "Loading items ${itemRange.first}..${itemRange.last}")

            val itemsFromDatabase = fetchFromDatabase(itemRange)
            val firstFetchedItem = itemRange.first + itemsFromDatabase.size

            val fetchedItems = if (itemsFromDatabase.size < itemRange.last - itemRange.first + 1) {
                val fetchRange = firstFetchedItem until firstFetchedItem + params.loadSize
                val firstPage = fetchRange.first / NETWORK_ITEMS_PER_PAGE + 1
                val lastPage = ((fetchRange.first + params.loadSize) / NETWORK_ITEMS_PER_PAGE + 1)
                    .coerceAtMost(networkConstants.pageCount)
                try {
                    if (SIMULATE_DELAY) delay(2000)
                    fetchFromNetwork(firstPage..lastPage)
                }
                catch (exception: Throwable) {
                    return@withContext LoadResult.Error(CharacterPagerLoadError())
                }
            } else emptyList()

            val itemsFromNetwork = fetchedItems
                .drop(firstItem % NETWORK_ITEMS_PER_PAGE)
                .take(params.loadSize)
            characterDao.insertOrUpdate(itemsFromNetwork)

            Log.i(
                tag,
                "Loaded ${itemsFromDatabase.size} from database, ${itemsFromNetwork.size} from network"
            )

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
                    .takeIf { it < (networkConstants.characterCount) }
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