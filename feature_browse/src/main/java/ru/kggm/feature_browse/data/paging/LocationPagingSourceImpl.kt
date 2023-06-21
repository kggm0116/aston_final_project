package ru.kggm.feature_browse.data.paging

import kotlinx.coroutines.flow.first
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.data.database.daos.LocationDao
import ru.kggm.feature_browse.data.entities.CharacterDataEntity
import ru.kggm.feature_browse.data.entities.LocationDataEntity
import ru.kggm.feature_browse.data.entities.LocationDataEntity.Companion.toDomainEntity
import ru.kggm.feature_browse.data.network.dtos.page.LocationPageDto
import ru.kggm.feature_browse.data.network.services.LocationService
import ru.kggm.feature_browse.domain.entities.LocationEntity
import ru.kggm.feature_browse.domain.paging.filters.LocationPagingFilters

class LocationPagingSourceImpl(
    filters: LocationPagingFilters,
    private val locationService: LocationService,
    private val locationDao: LocationDao
) : FilterPagingSourceImpl<LocationDataEntity, LocationPagingFilters, LocationEntity>(
    filters
) {
    companion object {
        private const val ITEMS_PER_NETWORK_PAGE: Int = 20
    }

    override val logTag = classTag()

    override suspend fun onClearCache() {
        locationDao.deleteAll()
    }

    override suspend fun fetchFromDatabase(range: IntRange) =
        locationDao.getRangeFiltered(
            skip = range.first,
            take = range.last - range.first + 1,
            ids = filters.ids,
            name = filters.nameQuery,
            type = filters.type,
            dimension = filters.dimension,
        ).first()

    override suspend fun fetchFromNetwork(
        itemRange: IntRange
    ): List<LocationDataEntity> {
        val fetchedItems = mutableListOf<LocationDataEntity>()
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
        locationService.getPage(
            pageNumber = pageNumber,
            type = filters.type,
            dimension = filters.dimension,
        )

    private fun mapNetworkPage(page: LocationPageDto) = page.results
        .filter { dto ->
            filters.nameQuery?.let { query ->
                dto.name.contains(query, ignoreCase = true)
            } ?: true
        }
        .map { it.toDataEntity() }

    override suspend fun cacheItems(items: List<LocationDataEntity>) {
        locationDao.insertOrUpdate(items)
    }

    override fun mapData(item: LocationDataEntity) = item.toDomainEntity()
    override val itemComparator = compareBy<LocationDataEntity> { it.id }
}