package ru.kggm.feature_browse.data.network.dtos

import ru.kggm.feature_browse.data.entities.CharacterDataEntity

data class CharacterDto(
    val id: Long,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String
) {
    fun toDataEntity() = CharacterDataEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image
    )
}