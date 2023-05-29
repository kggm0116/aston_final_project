package ru.kggm.feature_browse.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kggm.feature_browse.domain.repositories.LocationRepository
import javax.inject.Inject

class GetLocationById @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(id: Int) = withContext(Dispatchers.IO) {
        locationRepository.getById(id)
    }
}