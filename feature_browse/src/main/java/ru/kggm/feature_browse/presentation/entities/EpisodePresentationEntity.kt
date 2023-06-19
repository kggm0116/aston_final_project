package ru.kggm.feature_browse.presentation.entities

import com.google.gson.annotations.SerializedName
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.entities.EpisodeEntity
import java.time.LocalDate

data class EpisodePresentationEntity(
    val id: Long,
    val name: String,
    val airDate: String,
    val code: String,
    val characterIds: List<Int>
) {
    companion object {
        fun EpisodeEntity.toPresentationEntity() = EpisodePresentationEntity(
            id = id.toLong(),
            name = name,
            airDate = airDate,
            code = code,
            characterIds = characterIds
        )
    }
}
