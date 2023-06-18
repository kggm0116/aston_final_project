package ru.kggm.feature_browse.data.paging

import kotlinx.coroutines.flow.first
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.data.database.daos.CharacterDao
import ru.kggm.feature_browse.data.entities.CharacterDataEntity
import ru.kggm.feature_browse.data.entities.CharacterDataEntity.Companion.toDomainEntity
import ru.kggm.feature_browse.data.network.dtos.CharacterPageResponse
import ru.kggm.feature_browse.data.network.services.CharacterService
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.paging.CharacterPagingFilters

class CharacterPagingSourceImpl(
    filterParameters: CharacterPagingFilters,
    private val characterService: CharacterService,
    private val characterDao: CharacterDao,
) : FilterPagingSourceImpl<CharacterDataEntity, CharacterPagingFilters, CharacterPageResponse, CharacterEntity>(
    filterParameters
) {

    override val logTag = classTag()
    override val itemsPerNetworkPage: Int = 20
    override suspend fun onClearCache() {
        characterDao.deleteAll()
    }

    override fun getNetworkConstraints(response: CharacterPageResponse) = NetworkConstants(
        pageCount = response.info.pageCount,
        itemCount = response.info.characterCount
    )

    override suspend fun fetchFromDatabase(range: IntRange, filters: CharacterPagingFilters) =
        characterDao.getRangeFiltered(
            skip = range.first,
            take = range.last - range.first + 1,
            name = filters.name,
            status = filters.status,
            type = filters.type,
            species = filters.species,
            gender = filters.gender
        ).first()

    override suspend fun makeNetworkCall(pageNumber: Int) = with(filters) {
        characterService.getCharacterPage(
            pageNumber = pageNumber,
            name = name,
            status = status,
            type = type,
            species = species,
            gender = gender
        )
    }

    override suspend fun cacheItems(items: List<CharacterDataEntity>) {
        characterDao.insertOrUpdate(items)
    }

    override fun mapData(item: CharacterDataEntity) = item.toDomainEntity()
    override fun mapResponse(response: CharacterPageResponse) = response.results
        .map { it.toDataEntity() }
}