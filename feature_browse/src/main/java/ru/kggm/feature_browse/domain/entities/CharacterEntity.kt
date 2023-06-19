package ru.kggm.feature_browse.domain.entities

import com.google.gson.annotations.SerializedName

data class CharacterEntity(
    val id: Int,
    val name: String,
    val status: Status,
    val species: String,
    val type: String,
    val gender: Gender,
    val image: String
) {
    enum class Status { Alive, Dead, Unknown }
    enum class Gender { Female, Male, Genderless, Unknown }
}
