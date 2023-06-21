package ru.kggm.feature_browse.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kggm.feature_browse.data.database.daos.CharacterDao
import ru.kggm.feature_browse.data.database.daos.LocationDao
import ru.kggm.feature_browse.data.entities.CharacterDataEntity.Companion.toDomainEntity
import ru.kggm.feature_browse.data.entities.LocationDataEntity.Companion.toDomainEntity
import ru.kggm.feature_browse.data.network.services.CharacterService
import ru.kggm.feature_browse.data.network.services.LocationService
import ru.kggm.feature_browse.data.paging.CharacterPagingSourceImpl
import ru.kggm.feature_browse.data.paging.LocationPagingSourceImpl
import ru.kggm.feature_browse.domain.paging.filters.CharacterPagingFilters
import ru.kggm.feature_browse.domain.paging.filters.LocationPagingFilters
import ru.kggm.feature_browse.domain.repositories.CharacterRepository
import ru.kggm.feature_browse.domain.repositories.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationService: LocationService,
    private val locationDao: LocationDao
): LocationRepository {
    override fun pagingSource(
        filterParameters: LocationPagingFilters
    ) = LocationPagingSourceImpl(
        filterParameters,
        locationService,
        locationDao
    )

    override suspend fun getById(id: Int) = withContext(Dispatchers.IO) {
        when (val databaseEntity = locationDao.getById(id)) {
            null -> {
                val fetchedEntity = locationService.getById(listOf(id)).first().toDataEntity()
                locationDao.insertOrUpdate(fetchedEntity)
                return@withContext fetchedEntity.toDomainEntity()
            }
            else -> return@withContext databaseEntity.toDomainEntity()
        }
    }

}