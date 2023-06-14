package ru.kggm.feature_browse.data.network.dtos

data class CharacterPageResponse(
    val info: CharacterPageInfo,
    val results: List<CharacterDto>,
)
