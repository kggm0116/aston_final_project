package ru.kggm.feature_browse.domain.entities

data class CharacterEntity(
    val id: Int,
    val name: String,
    val status: Status,
    val species: String,
    val type: String,
    val gender: Gender,
    val image: String,
    val locationId: Int?,
    val originId: Int?,
    val episodeIds: List<Int>
) {
    enum class Status { Alive, Dead, Unknown }
    enum class Gender { Female, Male, Genderless, Unknown }
}
