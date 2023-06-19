package ru.kggm.feature_browse.data.paging

import kotlinx.coroutines.flow.first
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.data.database.daos.EpisodeDao
import ru.kggm.feature_browse.data.entities.EpisodeDataEntity
import ru.kggm.feature_browse.data.entities.EpisodeDataEntity.Companion.toDomainEntity
import ru.kggm.feature_browse.data.network.dtos.page.EpisodePageDto
import ru.kggm.feature_browse.data.network.services.EpisodeService
import ru.kggm.feature_browse.domain.entities.EpisodeEntity
import ru.kggm.feature_browse.domain.paging.filters.EpisodePagingFilters

class EpisodePagingSourceImpl(
    filters: EpisodePagingFilters,
    private val episodeService: EpisodeService,
    private val episodeDao: EpisodeDao
) : FilterPagingSourceImpl<EpisodeDataEntity, EpisodePagingFilters, EpisodePageDto, EpisodeEntity>(
    filters
) {

    override val logTag = classTag()
    override val itemsPerNetworkPage: Int = 20
    override suspend fun onClearCache() {
        episodeDao.deleteAll()
    }

    override fun getNetworkConstraints(response: EpisodePageDto) = NetworkConstants(
        pageCount = response.info.pageCount,
        itemCount = response.info.itemCount
    )

    override suspend fun fetchFromDatabase(range: IntRange) =
        episodeDao.getRangeFiltered(
            skip = range.first,
            take = range.last - range.first + 1,
            name = filters.nameQuery,
            code = filters.code
        ).first()

    override suspend fun fetchNetworkPage(pageNumber: Int) =
        episodeService.getPage(
            pageNumber = pageNumber,
            code = filters.code,
        )

    override suspend fun cacheItems(items: List<EpisodeDataEntity>) {
        episodeDao.insertOrUpdate(items)
    }

    override fun mapData(item: EpisodeDataEntity) = item.toDomainEntity()

    override fun mapNetworkPage(page: EpisodePageDto) = page.results
        .filter { dto ->
            filters.nameQuery?.let { query ->
                dto.name.contains(query, ignoreCase = true)
            } ?: true
        }
        .map { it.toDataEntity() }

    override val itemComparator = compareBy<EpisodeDataEntity> { it.id }
}