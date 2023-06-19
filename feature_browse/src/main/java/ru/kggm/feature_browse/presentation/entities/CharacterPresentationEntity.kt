package ru.kggm.feature_browse.presentation.entities

import ru.kggm.feature_browse.domain.entities.CharacterEntity

data class CharacterPresentationEntity(
    val id: Long,
    val name: String,
    val status: CharacterEntity.Status,
    val species: String,
    val type: String,
    val gender: CharacterEntity.Gender,
    val image: String
) {
    companion object {
        fun CharacterEntity.toPresentationEntity() = CharacterPresentationEntity(
            id = id.toLong(),
            name = name,
            status = status,
            species = species,
            type = type,
            gender = gender,
            image = image
        )
    }
}
