package ru.kggm.feature_browse.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kggm.feature_browse.data.database.SharedDatabase
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.entities.EpisodeEntity
import ru.kggm.feature_browse.domain.entities.LocationEntity
import java.time.LocalDate

@Entity(tableName = SharedDatabase.TABLE_EPISODE)
data class EpisodeDataEntity(
    @PrimaryKey @ColumnInfo(name = COL_ID)
    val id: Int,
    @ColumnInfo(name = COL_NAME)
    val name: String,
    @ColumnInfo(name = COL_AIR_DATE)
    val airDate: LocalDate,
    @ColumnInfo(name = COL_CODE)
    val code: String,
    @ColumnInfo(name = COL_CHARACTERS)
    val characterIds: List<Int>
) {
    companion object {
        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_AIR_DATE = "air_date"
        const val COL_CODE = "code"
        const val COL_CHARACTERS = "characters"

        fun EpisodeDataEntity.toDomainEntity() = EpisodeEntity(
            id = id,
            name = name,
            airDate = airDate,
            code = code,
            characterIds = characterIds
        )
    }

}