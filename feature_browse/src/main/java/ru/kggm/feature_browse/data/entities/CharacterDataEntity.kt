package ru.kggm.feature_browse.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kggm.feature_browse.data.database.SharedDatabase
import ru.kggm.feature_browse.domain.entities.CharacterEntity

@Entity(tableName = SharedDatabase.TABLE_CHARACTER)
data class CharacterDataEntity(
    @PrimaryKey @ColumnInfo(name = COL_ID)
    val id: Int,
    @ColumnInfo(name = COL_NAME)
    val name: String,
    @ColumnInfo(name = COL_STATUS)
    val status: CharacterEntity.Status,
    @ColumnInfo(name = COL_SPECIES)
    val species: String,
    @ColumnInfo(name = COL_GENDER)
    val gender: CharacterEntity.Gender,
    @ColumnInfo(name = COL_TYPE)
    val type: String,
    @ColumnInfo(name = COL_IMAGE)
    val image: String
) {
    companion object {
        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_STATUS = "status"
        const val COL_SPECIES = "species"
        const val COL_GENDER = "gender"
        const val COL_TYPE = "type"
        const val COL_IMAGE = "image"

        fun CharacterDataEntity.toDomainEntity() = CharacterEntity(
            id = id.toInt(),
            name = name,
            status = status,
            species = species,
            type = type,
            gender = gender,
            image = image
        )
    }

}