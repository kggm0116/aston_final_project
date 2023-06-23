package ru.kggm.feature_browse.presentation.entities

import ru.kggm.feature_browse.domain.entities.EpisodeEntity

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
