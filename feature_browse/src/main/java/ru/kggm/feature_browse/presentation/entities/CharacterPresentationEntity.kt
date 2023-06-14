package ru.kggm.feature_browse.presentation.entities

import ru.kggm.feature_browse.domain.entities.CharacterEntity

data class CharacterPresentationEntity(
    val id: Long,
    val name: String,
    val status: String,
    val species: String,
    val gender: String
) {
    companion object {
        fun CharacterEntity.toPresentationEntity() = CharacterPresentationEntity(
            id = id.toLong(),
            name = name,
            status = status,
            species = species,
            gender = gender
        )
    }
}
