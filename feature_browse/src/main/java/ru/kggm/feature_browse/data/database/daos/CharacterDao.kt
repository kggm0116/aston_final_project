package ru.kggm.feature_browse.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kggm.core.data.database.daos.BaseDao
import ru.kggm.feature_browse.data.entities.CharacterDataEntity

@Dao
interface CharacterDao: BaseDao<CharacterDataEntity> {
    @Query("SELECT * FROM ${CharacterDataEntity.TABLE}")
    fun getAll(): Flow<List<CharacterDataEntity>>

    @Query("SELECT * FROM ${CharacterDataEntity.TABLE} LIMIT :take OFFSET :skip")
    fun getRange(skip: Int, take: Int): Flow<List<CharacterDataEntity>>

    @Query("SELECT * FROM ${CharacterDataEntity.TABLE} WHERE ${CharacterDataEntity.ID} = :id")
    suspend fun getById(id: Int): CharacterDataEntity?

    @Query("DELETE FROM ${CharacterDataEntity.TABLE}")
    suspend fun deleteAll()
}