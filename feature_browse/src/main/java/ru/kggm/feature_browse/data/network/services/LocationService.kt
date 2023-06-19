package ru.kggm.feature_browse.data.network.services

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.kggm.feature_browse.data.network.dtos.entity.CharacterDto
import ru.kggm.feature_browse.data.network.dtos.entity.LocationDto
import ru.kggm.feature_browse.data.network.dtos.page_response.CharacterPageResponse
import ru.kggm.feature_browse.data.network.dtos.page_response.LocationPageResponse
import ru.kggm.feature_browse.domain.entities.CharacterEntity

interface LocationService {

    @GET("location")
    suspend fun getPage(
        @Query("page") pageNumber: Int = 1,
        @Query("name") name: String? = null,
        @Query("type") type: String? = null,
        @Query("dimension") dimension: String? = null,
    ): LocationPageResponse

    @GET("location/{id}")
    suspend fun getById(@Path("id") id: Int): LocationDto
}