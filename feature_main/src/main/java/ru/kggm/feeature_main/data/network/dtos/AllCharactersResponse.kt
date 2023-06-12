package ru.kggm.feeature_main.data.network.dtos

import com.google.gson.annotations.SerializedName

data class AllCharactersResponse(
    @SerializedName("results")
    val results: List<CharacterDto>
)
