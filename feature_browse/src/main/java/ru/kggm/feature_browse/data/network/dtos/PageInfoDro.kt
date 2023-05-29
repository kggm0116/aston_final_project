package ru.kggm.feature_browse.data.network.dtos

import com.google.gson.annotations.SerializedName

data class PageInfoDro(
    @SerializedName("count")
    val itemCount: Int,
    @SerializedName("pages")
    val pageCount: Int
)