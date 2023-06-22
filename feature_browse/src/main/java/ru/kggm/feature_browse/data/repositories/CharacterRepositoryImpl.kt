package ru.kggm.feature_browse.data.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.data.database.daos.CharacterDao
import ru.kggm.feature_browse.data.entities.CharacterDataEntity
import ru.kggm.feature_browse.data.entities.CharacterDataEntity.Companion.toDomainEntity
import ru.kggm.feature_browse.data.network.services.CharacterService
import ru.kggm.feature_browse.data.paging.CharacterPagingSourceImpl
import ru.kggm.feature_browse.domain.paging.filters.CharacterPagingFilters
import ru.kggm.feature_browse.domain.repositories.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterService: CharacterService,
    private val characterDao: CharacterDao
): CharacterRepository {

    companion object {
        const val SIMULATED_DELAY_MS = 1000L
    }

    override fun pagingSource(
        filterParameters: CharacterPagingFilters
    ) = CharacterPagingSourceImpl(
        filterParameters,
        characterService,
        characterDao
    )

    override suspend fun getById(id: Int) = withContext(Dispatchers.IO) {
        when (val fromNetwork = getFromNetwork(id)) {
            null -> {
                val fromDatabase = characterDao.getById(id)
                return@withContext fromDatabase?.toDomainEntity()
            }
            else -> {
                characterDao.insertOrUpdate(fromNetwork)
                return@withContext fromNetwork.toDomainEntity()
            }
        }
    }

    private suspend fun getFromNetwork(id: Int): CharacterDataEntity? {
        return try {
            delay(SIMULATED_DELAY_MS)
            characterService.getById(listOf(id)).first().toDataEntity()
        }
        catch (throwable: Throwable) {
            Log.i(classTag(), "Error in service call:\n${throwable.stackTraceToString()}")
            return null
        }
    }
}