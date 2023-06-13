package ru.kggm.feeature_characters.data.network.dtos

import com.google.gson.annotations.SerializedName
import ru.kggm.feeature_characters.data.entities.CharacterDataEntity

data class CharacterDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("gender")
    val gender: String
) {
    fun toDataEntity() = CharacterDataEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender
    )
}