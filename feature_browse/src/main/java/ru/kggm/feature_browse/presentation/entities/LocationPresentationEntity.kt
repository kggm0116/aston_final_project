package ru.kggm.feature_browse.presentation.entities

import ru.kggm.feature_browse.domain.entities.LocationEntity

data class LocationPresentationEntity(
    val id: Long,
    val name: String,
    val type: String,
    val dimension: String,
    val residentIds: List<Int>,
) {
    companion object {
        fun LocationEntity.toPresentationEntity() = LocationPresentationEntity(
            id = id.toLong(),
            name = name,
            type = type,
            dimension = dimension,
            residentIds = residentIds
        )
    }
}