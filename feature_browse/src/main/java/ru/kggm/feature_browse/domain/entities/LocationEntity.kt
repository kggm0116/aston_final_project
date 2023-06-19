package ru.kggm.feature_browse.domain.entities

import com.google.gson.annotations.SerializedName

data class LocationEntity(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residentIds: List<Int>,
)