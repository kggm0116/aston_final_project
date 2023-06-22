package ru.kggm.feature_browse.data.paging

import kotlinx.coroutines.flow.first
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.data.database.daos.CharacterDao
import ru.kggm.feature_browse.data.entities.CharacterDataEntity
import ru.kggm.feature_browse.data.entities.CharacterDataEntity.Companion.toDomainEntity
import ru.kggm.feature_browse.data.network.dtos.page.CharacterPageDto
import ru.kggm.feature_browse.data.network.services.CharacterService
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.paging.filters.CharacterPagingFilters

class CharacterPagingSourceImpl(
    filters: CharacterPagingFilters,
    private val characterService: CharacterService,
    private val characterDao: CharacterDao,
) : BasePagingSourceImpl<CharacterDataEntity, CharacterPagingFilters, CharacterEntity>(
    filters
) {
    companion object {
        private const val ITEMS_PER_NETWORK_PAGE: Int = 20
    }

    override val logTag = classTag()

    override suspend fun onClearCache() {
        characterDao.deleteAll()
    }

    override suspend fun fetchFromDatabase(range: IntRange) =
        characterDao.getRangeFiltered(
            skip = range.first,
            take = range.last - range.first + 1,
            filterIds = filters.ids != null,
            ids = filters.ids ?: emptyList(),
            name = filters.nameQuery,
            status = filters.status,
            type = filters.type,
            species = filters.species,
            gender = filters.gender
        ).first()

    override suspend fun fetchFromNetwork(
        itemRange: IntRange
    ): List<CharacterDataEntity> {
        return if (filters.ids != null) {
            fetchFromNetworkById(itemRange)
        } else {
            fetchFromNetworkByPage(itemRange)
        }
    }

    private suspend fun fetchFromNetworkById(itemRange: IntRange): List<CharacterDataEntity> {
        val remainingIds = filters.ids!!
            .drop(itemRange.first)
            .take(itemRange.last - itemRange.first)
        return if (remainingIds.none()) {
            emptyList()
        } else {
            characterService.getById(remainingIds)
                .map { it.toDataEntity() }
        }
    }

    private suspend fun fetchFromNetworkByPage(itemRange: IntRange): List<CharacterDataEntity> {
        val fetchedItems = mutableListOf<CharacterDataEntity>()
        val itemCount = itemRange.last - itemRange.first + 1
        var pageCount = Int.MAX_VALUE
        var iPage = itemRange.first / ITEMS_PER_NETWORK_PAGE + 1
        while (fetchedItems.size < itemCount) {
            if (iPage >= pageCount)
                break

            fetchNetworkPage(iPage++)
                .also { pageCount = it.info.pageCount }
                .let { response ->
                    mapNetworkPage(response).let { items ->
                        fetchedItems.addAll(items)
                    }
                }
        }
        return fetchedItems.take(itemCount)
    }

    private suspend fun fetchNetworkPage(pageNumber: Int) =
        characterService.getPage(
            pageNumber = pageNumber,
            status = filters.status,
            type = filters.type,
            species = filters.species,
            gender = filters.gender
        )

    private fun mapNetworkPage(page: CharacterPageDto) = page.results
        .filter { dto ->
            filters.nameQuery?.let { query ->
                dto.name.contains(query, ignoreCase = true)
            } ?: true
        }
        .map { it.toDataEntity() }

    override suspend fun cacheItems(items: List<CharacterDataEntity>) {
        characterDao.insertOrUpdate(items)
    }

    override fun mapData(item: CharacterDataEntity) = item.toDomainEntity()
    override val itemComparator = compareBy<CharacterDataEntity> { it.id }
}