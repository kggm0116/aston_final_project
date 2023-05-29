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
) : BasePagingSourceImpl<EpisodeDataEntity, EpisodePagingFilters, EpisodeEntity>(
    filters
) {
    companion object {
        private const val ITEMS_PER_NETWORK_PAGE: Int = 20
    }

    override val logTag = classTag()

    override suspend fun onClearCache() {
        episodeDao.deleteAll()
    }

    override suspend fun fetchFromDatabase(range: IntRange) = episodeDao.getRangeFiltered(
        skip = range.first,
        take = range.last - range.first + 1,
        filterIds = filters.ids != null,
        ids = filters.ids ?: emptyList(),
        name = filters.nameQuery,
        code = filters.code
    ).first()

    override suspend fun fetchFromNetwork(
        itemRange: IntRange
    ): List<EpisodeDataEntity> {
        return if (filters.ids != null) {
            fetchFromNetworkById(itemRange)
        } else {
            fetchFromNetworkByPage(itemRange)
        }
    }

    private suspend fun fetchFromNetworkById(itemRange: IntRange): List<EpisodeDataEntity> {
        val remainingIds = filters.ids!!
            .drop(itemRange.first)
            .take(itemRange.last - itemRange.first)
        return if (remainingIds.none()) {
            emptyList()
        } else {
            episodeService.getById(remainingIds)
                .map { it.toDataEntity() }
        }
    }

    private suspend fun fetchFromNetworkByPage(itemRange: IntRange): List<EpisodeDataEntity> {
        val fetchedItems = mutableListOf<EpisodeDataEntity>()
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
        episodeService.getPage(
            pageNumber = pageNumber,
            code = filters.code,
        )

    private fun mapNetworkPage(page: EpisodePageDto) = page.results
        .filter { dto ->
            filters.nameQuery?.let { query ->
                dto.name.contains(query, ignoreCase = true)
            } ?: true
        }
        .map { it.toDataEntity() }

    override suspend fun cacheItems(items: List<EpisodeDataEntity>) {
        episodeDao.insertOrUpdate(items)
    }

    override fun mapData(item: EpisodeDataEntity) = item.toDomainEntity()
    override val itemComparator = compareBy<EpisodeDataEntity> { it.id }
}