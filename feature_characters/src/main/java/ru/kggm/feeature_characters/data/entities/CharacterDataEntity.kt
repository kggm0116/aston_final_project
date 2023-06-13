package ru.kggm.feeature_characters.data.entities

import ru.kggm.feeature_characters.domain.entities.CharacterEntity

data class CharacterDataEntity(
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