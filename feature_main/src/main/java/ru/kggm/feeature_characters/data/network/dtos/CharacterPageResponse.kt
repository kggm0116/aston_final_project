package ru.kggm.feeature_characters.data.network.dtos

data class CharacterPageResponse(
    val info: CharacterPageInfo,
    val results: List<CharacterDto>,
)
