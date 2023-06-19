package ru.kggm.feature_browse.data.network.dtos.page_response

import ru.kggm.feature_browse.data.network.dtos.PageInfo
import ru.kggm.feature_browse.data.network.dtos.entity.EpisodeDto

data class EpisodePageResponse(
    val info: PageInfo,
    val results: List<EpisodeDto>,
)