package ru.kggm.feeature_characters.data.network.services

import retrofit2.http.GET
import retrofit2.http.Query
import ru.kggm.feeature_characters.data.network.dtos.CharacterPageResponse
import java.util.concurrent.CompletableFuture

interface CharacterService {
    @GET("character")
    fun getCharacterPage(
        @Query("page") pageNumber: Int
    ): CompletableFuture<CharacterPageResponse>
}