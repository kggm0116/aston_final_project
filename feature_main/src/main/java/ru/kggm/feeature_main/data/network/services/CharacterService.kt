package ru.kggm.feeature_main.data.network.services

import retrofit2.Call
import retrofit2.http.GET
import ru.kggm.feeature_main.data.network.dtos.AllCharactersResponse
import java.util.concurrent.CompletableFuture

interface CharacterService {
    @GET("character")
    fun getAllCharacters(): CompletableFuture<AllCharactersResponse>
}