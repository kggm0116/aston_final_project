package ru.kggm.feature_browse.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kggm.core.data.database.daos.BaseDao
import ru.kggm.feature_browse.data.database.SharedDatabase
import ru.kggm.feature_browse.data.entities.CharacterDataEntity
import ru.kggm.feature_browse.domain.entities.CharacterEntity

@Dao
interface CharacterDao : BaseDao<CharacterDataEntity> {
    @Query("SELECT * FROM ${SharedDatabase.TABLE_CHARACTER}")
    fun getAll(): Flow<List<CharacterDataEntity>>

    @Query(
        "SELECT * FROM ${SharedDatabase.TABLE_CHARACTER} WHERE " +
                "(:name IS NULL OR ${CharacterDataEntity.COL_NAME}=:name) AND " +
                "(:status IS NULL OR ${CharacterDataEntity.COL_STATUS}=:status) AND " +
                "(:type IS NULL OR ${CharacterDataEntity.COL_TYPE}=:type) AND " +
                "(:species IS NULL OR ${CharacterDataEntity.COL_SPECIES}=:species) AND " +
                "(:gender IS NULL OR ${CharacterDataEntity.COL_GENDER}=:gender) " +
                "LIMIT :take OFFSET :skip"
    )
    fun getRangeFiltered(
        skip: Int,
        take: Int,
        name: String?,
        status: CharacterEntity.Status?,
        type: String?,
        species: String?,
        gender: CharacterEntity.Gender?,
    ): Flow<List<CharacterDataEntity>>

    @Query("SELECT * FROM ${SharedDatabase.TABLE_CHARACTER} WHERE ${CharacterDataEntity.COL_ID} = :id")
    suspend fun getById(id: Long): CharacterDataEntity?

    @Query("DELETE FROM ${SharedDatabase.TABLE_CHARACTER}")
    suspend fun deleteAll()
}