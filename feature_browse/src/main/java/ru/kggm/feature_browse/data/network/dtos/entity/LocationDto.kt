package ru.kggm.feature_browse.data.network.dtos.entity

import com.google.gson.annotations.SerializedName
import ru.kggm.feature_browse.data.entities.CharacterDataEntity
import ru.kggm.feature_browse.data.entities.LocationDataEntity
import ru.kggm.feature_browse.domain.entities.CharacterEntity

data class LocationDto(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residentIds: List<Int>,
) {
    fun toDataEntity() = LocationDataEntity(
        id = id,
        name = name,
        type = type,
        dimension = dimension,
        residentIds = residentIds
    )
}