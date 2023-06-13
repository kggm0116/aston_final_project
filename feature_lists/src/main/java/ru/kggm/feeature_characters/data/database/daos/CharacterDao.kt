package ru.kggm.feeature_characters.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import ru.kggm.core.data.database.daos.BaseDao
import ru.kggm.feeature_characters.data.entities.CharacterDataEntity

@Dao
interface CharacterDao: BaseDao<CharacterDataEntity> {
    @Query("SELECT * FROM ${CharacterDataEntity.TABLE}")
    suspend fun getAll(): List<CharacterDataEntity>

    @Query("SELECT * FROM ${CharacterDataEntity.TABLE} WHERE ${CharacterDataEntity.ID} = :id")
    suspend fun getById(id: Int): CharacterDataEntity
}