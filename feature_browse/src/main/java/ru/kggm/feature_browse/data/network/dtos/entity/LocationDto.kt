package ru.kggm.feature_browse.data.network.dtos.entity

import com.google.gson.annotations.SerializedName
import ru.kggm.feature_browse.data.entities.CharacterDataEntity
import ru.kggm.feature_browse.data.entities.LocationDataEntity
import ru.kggm.feature_browse.data.network.services.CharacterService.Companion.getCharacterId
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import java.net.URL

data class LocationDto(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    @SerializedName("residents")
    val residentUrls: List<String>,
) {
    fun toDataEntity() = LocationDataEntity(
        id = id,
        name = name,
        type = type,
        dimension = dimension,
        residentIds = residentUrls.map { it.getCharacterId() }
    )
}