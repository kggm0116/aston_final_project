package ru.kggm.feature_browse.data.network.services

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.kggm.feature_browse.data.network.dtos.CharacterDto
import ru.kggm.feature_browse.data.network.dtos.CharacterPageResponse
import java.util.concurrent.CompletableFuture

interface CharacterService {
    @GET("character")
    fun getCharacterPage(
        @Query("page") pageNumber: Int
    ): CompletableFuture<CharacterPageResponse>

    @GET("character/{id}")
    fun getById(
        @Path("id") id: Long
    ): CompletableFuture<CharacterDto>
}