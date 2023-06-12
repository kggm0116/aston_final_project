package ru.kggm.feeature_main.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kggm.feeature_main.data.network.services.CharacterService

private const val BASE_URL = "https://rickandmortyapi.com/api/"
object Network {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val characterService: CharacterService = retrofit.create(CharacterService::class.java)
}