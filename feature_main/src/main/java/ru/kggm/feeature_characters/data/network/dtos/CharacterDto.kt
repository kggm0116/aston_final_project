package ru.kggm.feeature_characters.data.network.dtos

import com.google.gson.annotations.SerializedName
import ru.kggm.feeature_characters.data.entities.CharacterDataEntity

data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,

) {
    fun toDataEntity() = CharacterDataEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender
    )
}