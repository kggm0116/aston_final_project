package ru.kggm.feature_browse.data.network.services

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.kggm.feature_browse.data.network.dtos.CharacterDto
import ru.kggm.feature_browse.data.network.dtos.CharacterPageResponse
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import java.util.concurrent.CompletableFuture

interface CharacterService {

    // If nothing matches filters, returns plain error object
    @GET("character")
    fun getCharacterPage(
        @Query("page") pageNumber: Int = 1,
        @Query("name") name: String? = null,
        @Query("status") status: CharacterEntity.Status? = null,
        @Query("species") species: String? = null,
        @Query("type") type: String? = null,
        @Query("gender") gender: CharacterEntity.Gender? = null,
    ): CompletableFuture<CharacterPageResponse>

    @GET("character/{id}")
    fun getById(@Path("id") id: Long): CompletableFuture<CharacterDto>
}