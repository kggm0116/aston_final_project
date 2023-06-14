package ru.kggm.feature_browse.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kggm.feature_browse.data.database.daos.CharacterDao
import ru.kggm.feature_browse.data.entities.CharacterDataEntity.Companion.toDomainEntity
import ru.kggm.feature_browse.data.network.services.CharacterService
import ru.kggm.feature_browse.data.paging.CharacterPagingSourceImpl
import ru.kggm.feature_browse.domain.repositories.CharacterRepository
import ru.kggm.feature_browse.domain.repositories.paging.CharacterPagingSource
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterService: CharacterService,
    private val characterDao: CharacterDao
): CharacterRepository {
    override fun characterPagingSource(): CharacterPagingSource =
        CharacterPagingSourceImpl(characterService, characterDao)

    override suspend fun getById(id: Int) = withContext(Dispatchers.IO) {
        when (val databaseEntity = characterDao.getById(id)) {
            null -> {
                val fetchedEntity = characterService.getById(id).join()
                    .toDataEntity()
                characterDao.add(fetchedEntity)
                return@withContext fetchedEntity.toDomainEntity()
            }
            else -> return@withContext databaseEntity.toDomainEntity()
        }
    }

}