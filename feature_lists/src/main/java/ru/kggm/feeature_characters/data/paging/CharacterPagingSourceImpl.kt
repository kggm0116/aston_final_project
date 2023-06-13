package ru.kggm.feeature_characters.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kggm.feeature_characters.data.entities.CharacterDataEntity
import ru.kggm.feeature_characters.data.network.dtos.CharacterPageResponse
import ru.kggm.feeature_characters.data.network.services.CharacterService
import ru.kggm.feeature_characters.domain.entities.CharacterEntity
import javax.inject.Inject
import kotlin.math.max

class CharacterPagingSourceImpl @Inject constructor(
    private val characterService: CharacterService
) : PagingSource<Int, CharacterEntity>() {

    companion object {
        private const val NETWORK_ITEMS_PER_PAGE = 20
        private const val STARTING_KEY = 0
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>): Int? {
        return 0
    }

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
            Log.i("Character paging source", "Loading items ${itemRange.first}..${itemRange.last}")

            val firstPage = itemRange.first / NETWORK_ITEMS_PER_PAGE + 1
            val lastPage = ((itemRange.first + params.loadSize) / NETWORK_ITEMS_PER_PAGE + 1)
                .coerceAtMost(networkConstants?.pageCount ?: Int.MAX_VALUE)

            val fetchedItems = mutableListOf<CharacterDataEntity>()
            for (iPage in firstPage .. lastPage) {
                try {
                    characterService.getCharacterPage(iPage).join()
                        .also { trySetNetworkConstants(it) }
                        .results
                        .map { it.toDataEntity() }
                        .let { fetchedItems.addAll(it) }
                } catch (e: Exception) {
                    return@withContext LoadResult.Error(e)
                }
            }

            val requestedItems = fetchedItems
                .drop(firstItem % NETWORK_ITEMS_PER_PAGE)
                .take(params.loadSize)

            return@withContext LoadResult.Page(
                data = requestedItems
                    .map { it.toDomainEntity() },
                prevKey = when (itemRange.first) {
                    STARTING_KEY -> null
                    else -> when (val prevKey = max(STARTING_KEY, itemRange.first - params.loadSize)) {
                        STARTING_KEY -> null
                        else -> prevKey
                    }
                },
                nextKey = (itemRange.last + 1)
                    .takeIf { it < (networkConstants?.characterCount ?: Int.MAX_VALUE) }
            )
        }
}