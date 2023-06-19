package ru.kggm.feature_browse.data.network.dtos.entity

import com.google.gson.annotations.SerializedName
import ru.kggm.feature_browse.data.entities.CharacterDataEntity
import ru.kggm.feature_browse.data.entities.EpisodeDataEntity
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import java.time.LocalDate

data class EpisodeDto(
    val id: Int,
    val name: String,
    val airDate: LocalDate,
    val code: String,
    val characterIds: List<Int>
) {
    fun toDataEntity() = EpisodeDataEntity(
        id = id,
        name = name,
        airDate = airDate,
        code = code,
        characterIds = characterIds
    )
}