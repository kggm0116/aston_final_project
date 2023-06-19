package ru.kggm.feature_browse.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kggm.core.data.database.daos.BaseDao
import ru.kggm.feature_browse.data.database.SharedDatabase
import ru.kggm.feature_browse.data.entities.CharacterDataEntity
import ru.kggm.feature_browse.data.entities.EpisodeDataEntity
import ru.kggm.feature_browse.data.entities.LocationDataEntity
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import java.time.LocalDate

@Dao
interface EpisodeDao : BaseDao<EpisodeDataEntity> {

    companion object {
        private const val EPISODE = SharedDatabase.TABLE_EPISODE
        private const val ID = EpisodeDataEntity.COL_ID
        private const val NAME = EpisodeDataEntity.COL_NAME
        private const val CODE = EpisodeDataEntity.COL_CODE
    }

    @Query("SELECT * FROM $EPISODE")
    fun getAll(): Flow<List<EpisodeDataEntity>>



    @Query(
        "SELECT * FROM $EPISODE WHERE " +
                "(:name IS NULL OR $NAME LIKE '%'||:name||'%') AND " +
                "(:code IS NULL OR $CODE = :code) " +
                "LIMIT :take OFFSET :skip"
    )
    fun getRangeFiltered(
        skip: Int,
        take: Int,
        name: String?,
        code: String?,
    ): Flow<List<EpisodeDataEntity>>

    @Query("SELECT * FROM $EPISODE WHERE $ID = :id")
    suspend fun getById(id: Int): EpisodeDataEntity?

    @Query("DELETE FROM $EPISODE")
    suspend fun deleteAll()
}