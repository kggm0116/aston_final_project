package ru.kggm.feature_browse.data.network.dtos.page_response

import ru.kggm.feature_browse.data.network.dtos.PageInfo
import ru.kggm.feature_browse.data.network.dtos.entity.CharacterDto

data class CharacterPageResponse(
    val info: PageInfo,
    val results: List<CharacterDto>,
)
