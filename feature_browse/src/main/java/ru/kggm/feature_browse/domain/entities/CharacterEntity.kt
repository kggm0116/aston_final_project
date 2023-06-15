package ru.kggm.feature_browse.domain.entities

data class CharacterEntity(
    val id: Long,
    val name: String,
    val status: Status,
    val species: String,
    val gender: Gender,
    val image: String
) {
    enum class Status { Alive, Dead, Unknown }
    enum class Gender { Female, Male, Genderless, Unknown }
}
