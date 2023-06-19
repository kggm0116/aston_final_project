package ru.kggm.feature_browse.data.network.dtos.entity

import com.google.gson.annotations.SerializedName
import ru.kggm.feature_browse.data.entities.CharacterDataEntity
import ru.kggm.feature_browse.data.entities.LocationDataEntity
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import java.net.URL

data class LocationShortDto(
    val name: String,
    val url: String
)