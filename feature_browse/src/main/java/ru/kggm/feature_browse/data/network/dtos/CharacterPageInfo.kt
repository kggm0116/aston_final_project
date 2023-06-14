package ru.kggm.feature_browse.data.network.dtos

import com.google.gson.annotations.SerializedName

data class CharacterPageInfo(
    @SerializedName("count")
    val characterCount: Int,
    @SerializedName("pages")
    val pageCount: Int
)