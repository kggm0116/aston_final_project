package ru.kggm.feature_browse.data.network.dtos.page

import ru.kggm.feature_browse.data.network.dtos.PageInfoDro
import ru.kggm.feature_browse.data.network.dtos.entity.EpisodeDto

data class EpisodePageDto(
    val info: PageInfoDro,
    val results: List<EpisodeDto>,
)