package ru.kggm.feature_browse.domain.entities

data class LocationEntity(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residentIds: List<Int>,
)