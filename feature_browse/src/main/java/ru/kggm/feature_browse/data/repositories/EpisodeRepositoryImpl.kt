package ru.kggm.feature_browse.data.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.data.database.daos.EpisodeDao
import ru.kggm.feature_browse.data.entities.EpisodeDataEntity
import ru.kggm.feature_browse.data.entities.EpisodeDataEntity.Companion.toDomainEntity
import ru.kggm.feature_browse.data.network.services.EpisodeService
import ru.kggm.feature_browse.data.paging.EpisodePagingSourceImpl
import ru.kggm.feature_browse.domain.paging.filters.EpisodePagingFilters
import ru.kggm.feature_browse.domain.repositories.EpisodeRepository
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    private val episodeService: EpisodeService,
    private val episodeDao: EpisodeDao
): EpisodeRepository {

    companion object {
        const val SIMULATED_DELAY_MS = 0L
    }

    override fun pagingSource(
        filterParameters: EpisodePagingFilters
    ) = EpisodePagingSourceImpl(
        filterParameters,
        episodeService,
        episodeDao
    )

    override suspend fun getById(id: Int) = withContext(Dispatchers.IO) {
        when (val fromNetwork = getFromNetwork(id)) {
            null -> {
                val fromDatabase = episodeDao.getById(id)
                return@withContext fromDatabase?.toDomainEntity()
            }
            else -> {
                episodeDao.insertOrUpdate(fromNetwork)
                return@withContext fromNetwork.toDomainEntity()
            }
        }
    }

    private suspend fun getFromNetwork(id: Int): EpisodeDataEntity? {
        return try {
            delay(CharacterRepositoryImpl.SIMULATED_DELAY_MS)
            episodeService.getById(listOf(id)).first().toDataEntity()
        }
        catch (throwable: Throwable) {
            Log.i(classTag(), "Error in service call:\n${throwable.stackTraceToString()}")
            return null
        }
    }
}