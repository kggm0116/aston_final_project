package ru.kggm.feature_browse.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kggm.core.data.database.daos.BaseDao
import ru.kggm.feature_browse.data.database.SharedDatabase
import ru.kggm.feature_browse.data.entities.LocationDataEntity

@Dao
interface LocationDao : BaseDao<LocationDataEntity> {

    companion object {
        private const val LOCATION = SharedDatabase.TABLE_LOCATION
        private const val ID = LocationDataEntity.COL_ID
        private const val NAME = LocationDataEntity.COL_NAME
        private const val TYPE = LocationDataEntity.COL_TYPE
        private const val DIMENSION = LocationDataEntity.COL_DIMENSION
    }

    @Query("SELECT * FROM $LOCATION")
    fun getAll(): Flow<List<LocationDataEntity>>

    @Query(
        "SELECT * FROM $LOCATION WHERE " +
                "(:filterIds == 0 OR $ID IN (:ids)) AND " +
                "(:name IS NULL OR $NAME LIKE '%'||:name||'%') AND " +
                "(:type IS NULL OR $TYPE = :type) AND " +
                "(:dimension IS NULL OR $DIMENSION = :dimension) " +
                "LIMIT :take OFFSET :skip"
    )
    fun getRangeFiltered(
        skip: Int,
        take: Int,
        filterIds: Boolean = false, // Sqlite/Room seemingly can't check nullable list for NULL...
        ids: List<Int>,
        name: String?,
        type: String?,
        dimension: String?
    ): Flow<List<LocationDataEntity>>

    @Query("SELECT * FROM $LOCATION WHERE $ID = :id")
    suspend fun getById(id: Int): LocationDataEntity?

    @Query("DELETE FROM $LOCATION")
    suspend fun deleteAll()
}