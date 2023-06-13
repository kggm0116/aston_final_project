package ru.kggm.feeature_characters.data.network.services

import retrofit2.http.GET
import ru.kggm.feeature_characters.data.network.dtos.AllCharactersResponse
import java.util.concurrent.CompletableFuture

interface CharacterService {
    @GET("character")
    fun getAllCharacters(): CompletableFuture<AllCharactersResponse>
}