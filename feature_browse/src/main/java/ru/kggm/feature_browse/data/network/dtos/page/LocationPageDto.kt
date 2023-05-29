package ru.kggm.feature_browse.data.network.dtos.page

import ru.kggm.feature_browse.data.network.dtos.PageInfoDro
import ru.kggm.feature_browse.data.network.dtos.entity.LocationDto

data class LocationPageDto(
    val info: PageInfoDro,
    val results: List<LocationDto>,
)