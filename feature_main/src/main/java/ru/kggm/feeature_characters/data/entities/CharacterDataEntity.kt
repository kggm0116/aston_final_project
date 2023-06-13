package ru.kggm.feeature_characters.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kggm.feeature_characters.domain.entities.CharacterEntity

@Entity(tableName = CharacterDataEntity.TABLE)
data class CharacterDataEntity(
    @PrimaryKey @ColumnInfo(name = ID)
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String
) {
    companion object {
        const val TABLE = "character_table"
        const val ID = "id"

        fun CharacterDataEntity.toDomainEntity() = CharacterEntity(
            id = id,
            name = name,
            status = status,
            species = species,
            gender = gender
        )
    }

}