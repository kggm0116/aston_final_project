package ru.kggm.feeature_main.domain.entities

data class CharacterEntity(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String
)
