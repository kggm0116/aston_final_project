package ru.kggm.feature_browse.data.network.dtos.page_response

import ru.kggm.feature_browse.data.network.dtos.PageInfo
import ru.kggm.feature_browse.data.network.dtos.entity.LocationDto

data class LocationPageResponse(
    val info: PageInfo,
    val results: List<LocationDto>,
)