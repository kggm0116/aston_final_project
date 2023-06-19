package ru.kggm.feature_browse.data.paging

import kotlinx.coroutines.flow.first
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.data.database.daos.LocationDao
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
) : FilterPagingSourceImpl<LocationDataEntity, LocationPagingFilters, LocationPageDto, LocationEntity>(
    filters
) {

    override val logTag = classTag()
    override val itemsPerNetworkPage: Int = 20
    override suspend fun onClearCache() {
        locationDao.deleteAll()
    }

    override fun getNetworkConstraints(response: LocationPageDto) = NetworkConstants(
        pageCount = response.info.pageCount,
        itemCount = response.info.itemCount
    )

    override suspend fun fetchFromDatabase(range: IntRange) =
        locationDao.getRangeFiltered(
            skip = range.first,
            take = range.last - range.first + 1,
            name = filters.nameQuery,
            type = filters.type,
            dimension = filters.dimension,
        ).first()

    override suspend fun fetchNetworkPage(pageNumber: Int) =
        locationService.getPage(
            pageNumber = pageNumber,
            type = filters.type,
            dimension = filters.dimension,
        )

    override suspend fun cacheItems(items: List<LocationDataEntity>) {
        locationDao.insertOrUpdate(items)
    }

    override fun mapData(item: LocationDataEntity) = item.toDomainEntity()

    override fun mapNetworkPage(page: LocationPageDto) = page.results
        .filter { dto ->
            filters.nameQuery?.let { query ->
                dto.name.contains(query, ignoreCase = true)
            } ?: true
        }
        .map { it.toDataEntity() }

    override val itemComparator = compareBy<LocationDataEntity> { it.id }
}