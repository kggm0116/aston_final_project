package ru.kggm.feature_browse.data.network.dtos

data class BasePageResponse<TInfo : BasePageInfo, TDto: Base>(
    val info: CharacterPageInfo,
    val results: List<CharacterDto>,
)
