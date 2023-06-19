package ru.kggm.feature_browse.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kggm.feature_browse.data.database.daos.CharacterDao
import ru.kggm.feature_browse.data.database.daos.EpisodeDao
import ru.kggm.feature_browse.data.entities.CharacterDataEntity.Companion.toDomainEntity
import ru.kggm.feature_browse.data.entities.EpisodeDataEntity.Companion.toDomainEntity
import ru.kggm.feature_browse.data.network.services.CharacterService
import ru.kggm.feature_browse.data.network.services.EpisodeService
import ru.kggm.feature_browse.data.paging.CharacterPagingSourceImpl
import ru.kggm.feature_browse.data.paging.EpisodePagingSourceImpl
import ru.kggm.feature_browse.domain.paging.filters.CharacterPagingFilters
import ru.kggm.feature_browse.domain.paging.filters.EpisodePagingFilters
import ru.kggm.feature_browse.domain.repositories.CharacterRepository
import ru.kggm.feature_browse.domain.repositories.EpisodeRepository
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    private val EpisodeService: EpisodeService,
    private val EpisodeDao: EpisodeDao
): EpisodeRepository {
    override fun pagingSource(
        filterParameters: EpisodePagingFilters
    ) = EpisodePagingSourceImpl(
        filterParameters,
        EpisodeService,
        EpisodeDao
    )

    override suspend fun getById(id: Int) = withContext(Dispatchers.IO) {
        when (val databaseEntity = EpisodeDao.getById(id)) {
            null -> {
                val fetchedEntity = EpisodeService.getById(id).toDataEntity()
                EpisodeDao.insertOrUpdate(fetchedEntity)
                return@withContext fetchedEntity.toDomainEntity()
            }
            else -> return@withContext databaseEntity.toDomainEntity()
        }
    }

}