package ru.kggm.feature_browse.domain.entities

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class EpisodeEntity(
    val id: Int,
    val name: String,
    val airDate: String,
    val code: String,
    val characterIds: List<Int>
)
