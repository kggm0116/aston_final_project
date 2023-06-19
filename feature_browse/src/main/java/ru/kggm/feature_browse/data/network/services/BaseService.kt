package ru.kggm.feature_browse.data.network.services

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.kggm.feature_browse.data.network.dtos.entity.CharacterDto
import ru.kggm.feature_browse.data.network.dtos.page_response.CharacterPageResponse
import ru.kggm.feature_browse.domain.entities.CharacterEntity

interface BaseService {

    // If nothing matches filters, returns plain error object
    @GET("character")
    suspend fun getCharacterPage(
        @Query("page") pageNumber: Int = 1,
        @Query("name") name: String? = null,
        @Query("status") status: CharacterEntity.Status? = null,
        @Query("species") species: String? = null,
        @Query("type") type: String? = null,
        @Query("gender") gender: CharacterEntity.Gender? = null,
    ): CharacterPageResponse

    @GET("character/{id}")
    suspend fun getById(@Path("id") id: Long): CharacterDto
}