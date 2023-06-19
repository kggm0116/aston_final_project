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
) : FilterPagingSourceImpl<CharacterDataEntity, CharacterPagingFilters, CharacterPageDto, CharacterEntity>(
    filters
) {

    override val logTag = classTag()
    override val itemsPerNetworkPage: Int = 20
    override suspend fun onClearCache() {
        characterDao.deleteAll()
    }

    override fun getNetworkConstraints(response: CharacterPageDto) = NetworkConstants(
        pageCount = response.info.pageCount,
        itemCount = response.info.itemCount
    )

    override suspend fun fetchFromDatabase(range: IntRange) =
        characterDao.getRangeFiltered(
            skip = range.first,
            take = range.last - range.first + 1,
            name = filters.nameQuery,
            status = filters.status,
            type = filters.type,
            species = filters.species,
            gender = filters.gender
        ).first()

    override suspend fun fetchNetworkPage(pageNumber: Int) =
        characterService.getPage(
            pageNumber = pageNumber,
            status = filters.status,
            type = filters.type,
            species = filters.species,
            gender = filters.gender
        )
            .also {
                println()
            }

    override suspend fun cacheItems(items: List<CharacterDataEntity>) {
        characterDao.insertOrUpdate(items)
    }

    override fun mapData(item: CharacterDataEntity) = item.toDomainEntity()

    override fun mapNetworkPage(page: CharacterPageDto) = page.results
        .filter { dto ->
            filters.nameQuery?.let { query ->
                dto.name.contains(query, ignoreCase = true)
            } ?: true
        }
        .map { it.toDataEntity() }

    override val itemComparator = compareBy<CharacterDataEntity> { it.id }
}