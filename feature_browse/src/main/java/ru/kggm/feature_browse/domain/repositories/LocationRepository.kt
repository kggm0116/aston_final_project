package ru.kggm.feature_browse.domain.repositories

import ru.kggm.feature_browse.domain.entities.LocationEntity
import ru.kggm.feature_browse.domain.paging.FilterPagingSource
import ru.kggm.feature_browse.domain.paging.filters.LocationPagingFilters

typealias LocationPagingSource = FilterPagingSource<LocationEntity, LocationPagingFilters>

interface LocationRepository {
    fun pagingSource(filterParameters: LocationPagingFilters): LocationPagingSource

    suspend fun getById(id: Int): LocationEntity?
}