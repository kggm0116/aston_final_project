package ru.kggm.feeature_characters.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kggm.feeature_characters.domain.entities.CharacterEntity

@Entity
data class CharacterDataEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String
) {
    fun toDomainEntity() = CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender
    )
}