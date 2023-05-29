package ru.kggm.feature_browse.data.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.data.database.daos.LocationDao
import ru.kggm.feature_browse.data.entities.LocationDataEntity
import ru.kggm.feature_browse.data.entities.LocationDataEntity.Companion.toDomainEntity
import ru.kggm.feature_browse.data.network.services.LocationService
import ru.kggm.feature_browse.data.paging.LocationPagingSourceImpl
import ru.kggm.feature_browse.domain.paging.filters.LocationPagingFilters
import ru.kggm.feature_browse.domain.repositories.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationService: LocationService,
    private val locationDao: LocationDao
): LocationRepository {

    companion object {
        const val SIMULATED_DELAY_MS = 1000L
    }

    override fun pagingSource(
        filterParameters: LocationPagingFilters
    ) = LocationPagingSourceImpl(
        filterParameters,
        locationService,
        locationDao
    )

    override suspend fun getById(id: Int) = withContext(Dispatchers.IO) {
        when (val fromNetwork = getFromNetwork(id)) {
            null -> {
                val fromDatabase = locationDao.getById(id)
                return@withContext fromDatabase?.toDomainEntity()
            }
            else -> {
                locationDao.insertOrUpdate(fromNetwork)
                return@withContext fromNetwork.toDomainEntity()
            }
        }
    }

    private suspend fun getFromNetwork(id: Int): LocationDataEntity? {
        return try {
            delay(SIMULATED_DELAY_MS)
            locationService.getById(listOf(id)).first().toDataEntity()
        }
        catch (throwable: Throwable) {
            Log.i(classTag(), "Error in service call:\n${throwable.stackTraceToString()}")
            return null
        }
    }
}