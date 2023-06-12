package ru.kggm.feeature_main.presentation.entities

import ru.kggm.feeature_main.domain.entities.CharacterEntity

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
