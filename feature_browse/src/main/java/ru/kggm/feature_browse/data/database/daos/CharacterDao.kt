package ru.kggm.feature_browse.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kggm.core.data.database.daos.BaseDao
import ru.kggm.feature_browse.data.database.SharedDatabase
import ru.kggm.feature_browse.data.entities.CharacterDataEntity
import ru.kggm.feature_browse.data.entities.LocationDataEntity
import ru.kggm.feature_browse.domain.entities.CharacterEntity

@Dao
interface CharacterDao : BaseDao<CharacterDataEntity> {

    companion object {
        private const val CHARACTER = SharedDatabase.TABLE_CHARACTER
        private const val ID = CharacterDataEntity.COL_ID
        private const val NAME = CharacterDataEntity.COL_NAME
        private const val STATUS = CharacterDataEntity.COL_STATUS
        private const val TYPE = CharacterDataEntity.COL_TYPE
        private const val SPECIES = CharacterDataEntity.COL_SPECIES
        private const val GENDER = CharacterDataEntity.COL_GENDER
    }

    @Query("SELECT * FROM $CHARACTER")
    fun getAll(): Flow<List<CharacterDataEntity>>

    @Query(
        "SELECT * FROM $CHARACTER WHERE " +
                "(:ids IS NULL OR $ID IN (:ids)) AND " +
                "(:name IS NULL OR $NAME LIKE '%'||:name||'%') AND " +
                "(:status IS NULL OR $STATUS = :status) AND " +
                "(:type IS NULL OR $TYPE = :type) AND " +
                "(:species IS NULL OR $SPECIES = :species) AND " +
                "(:gender IS NULL OR $GENDER = :gender) " +
                "LIMIT :take OFFSET :skip"
    )
    fun getRangeFiltered(
        skip: Int,
        take: Int,
        ids: List<Int>?,
        name: String?,
        status: CharacterEntity.Status?,
        type: String?,
        species: String?,
        gender: CharacterEntity.Gender?,
    ): Flow<List<CharacterDataEntity>>

    @Query("SELECT * FROM $CHARACTER WHERE $ID = :id")
    suspend fun getById(id: Int): CharacterDataEntity?

    @Query("DELETE FROM $CHARACTER")
    suspend fun deleteAll()
}