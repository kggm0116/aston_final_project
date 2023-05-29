package ru.kggm.feature_browse.data.network.dtos.entity

import com.google.gson.annotations.SerializedName
import ru.kggm.feature_browse.data.entities.EpisodeDataEntity
import ru.kggm.feature_browse.data.network.services.CharacterService.Companion.getCharacterId

data class EpisodeDto(
    val id: Int,
    val name: String,
    @SerializedName("air_date")
    val airDate: String,
    @SerializedName("episode")
    val code: String,
    @SerializedName("characters")
    val characterUrls: List<String>,
) {
    fun toDataEntity() = EpisodeDataEntity(
        id = id,
        name = name,
        airDate = airDate,
        code = code,
        characterIds = characterUrls.map { it.getCharacterId() }
    )
}