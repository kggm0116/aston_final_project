package ru.kggm.feature_browse.data.network.dtos

import com.google.gson.annotations.SerializedName

interface BasePageInfo {
    val itemCount: Int
    val pageCount: Int
}
