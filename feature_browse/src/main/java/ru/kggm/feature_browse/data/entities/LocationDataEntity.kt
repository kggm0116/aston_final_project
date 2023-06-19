package ru.kggm.feature_browse.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kggm.feature_browse.data.database.SharedDatabase
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.entities.LocationEntity

@Entity(tableName = SharedDatabase.TABLE_LOCATION)
data class LocationDataEntity(
    @PrimaryKey @ColumnInfo(name = COL_ID)
    val id: Int,
    @ColumnInfo(name = COL_NAME)
    val name: String,
    @ColumnInfo(name = COL_TYPE)
    val type: String,
    @ColumnInfo(name = COL_DIMENSION)
    val dimension: String,
    @ColumnInfo(name = COL_RESIDENTS)
    val residentIds: List<Int>
) {
    companion object {
        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_TYPE = "type"
        const val COL_DIMENSION = "dimension"
        const val COL_RESIDENTS = "image"

        fun LocationDataEntity.toDomainEntity() = LocationEntity(
            id = id,
            name = name,
            type = type,
            dimension = dimension,
            residentIds = residentIds,
        )
    }

}