package ru.kggm.feature_browse.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kggm.feature_browse.data.database.daos.CharacterDao
import ru.kggm.feature_browse.data.entities.CharacterDataEntity.Companion.toDomainEntity
import ru.kggm.feature_browse.data.network.services.CharacterService
import ru.kggm.feature_browse.data.paging.CharacterPagingSourceImpl
import ru.kggm.feature_browse.domain.entities.CharacterFilterParameters
import ru.kggm.feature_browse.domain.repositories.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterService: CharacterService,
    private val characterDao: CharacterDao
): CharacterRepository {
    override fun characterPagingSource(
        filterParameters: CharacterFilterParameters
    ) = CharacterPagingSourceImpl(filterParameters, characterService, characterDao)

    override suspend fun getById(id: Long) = withContext(Dispatchers.IO) {
        when (val databaseEntity = characterDao.getById(id)) {
            null -> {
                val fetchedEntity = characterService.getById(id).toDataEntity()
                characterDao.insertOrUpdate(fetchedEntity)
                return@withContext fetchedEntity.toDomainEntity()
            }
            else -> return@withContext databaseEntity.toDomainEntity()
        }
    }

}