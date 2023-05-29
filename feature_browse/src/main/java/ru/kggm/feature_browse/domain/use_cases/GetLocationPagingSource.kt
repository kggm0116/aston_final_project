package ru.kggm.feature_browse.domain.use_cases

import ru.kggm.feature_browse.domain.paging.filters.LocationPagingFilters
import ru.kggm.feature_browse.domain.repositories.LocationPagingSource
import ru.kggm.feature_browse.domain.repositories.LocationRepository
import javax.inject.Inject

class GetLocationPagingSource @Inject constructor(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(filters: LocationPagingFilters) : LocationPagingSource =
        locationRepository.pagingSource(filters)
}