package ru.kggm.feature_browse.data.network.services

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.kggm.feature_browse.data.network.dtos.entity.LocationDto
import ru.kggm.feature_browse.data.network.dtos.page.LocationPageDto

interface LocationService {

    @GET("location")
    suspend fun getPage(
        @Query("page") pageNumber: Int = 1,
        @Query("name") name: String? = null,
        @Query("type") type: String? = null,
        @Query("dimension") dimension: String? = null,
    ): LocationPageDto

    @GET("location/{ids}")
    suspend fun getById(@Path("ids") id: List<Int>): List<LocationDto>

    companion object {
        private val locationIdRegex by lazy { Regex("https://rickandmortyapi.com/api/location/(?<id>\\d++)$") }
        fun String.getLocationId(): Int = locationIdRegex.matchEntire(this)?.let { matchResult ->
            matchResult.groups["id"]?.value?.toIntOrNull()
        }  ?: throw IllegalArgumentException("Can't extract location id from '$this'")
    }
}