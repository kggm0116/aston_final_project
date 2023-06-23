package ru.kggm.feature_browse.domain.entities

data class EpisodeEntity(
    val id: Int,
    val name: String,
    val airDate: String,
    val code: String,
    val characterIds: List<Int>
)
