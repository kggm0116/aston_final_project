package ru.kggm.feature_browse.data.network.services

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.kggm.feature_browse.data.network.dtos.entity.CharacterDto
import ru.kggm.feature_browse.data.network.dtos.page.CharacterPageDto
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.net.URL

interface CharacterService {

    @GET("character")
    suspend fun getPage(
        @Query("page") pageNumber: Int = 1,
        @Query("name") name: String? = null,
        @Query("status") status: CharacterEntity.Status? = null,
        @Query("species") species: String? = null,
        @Query("type") type: String? = null,
        @Query("gender") gender: CharacterEntity.Gender? = null,
    ): CharacterPageDto

    @GET("character/{id}")
    suspend fun getById(@Path("id") id: Int): CharacterDto

    companion object {
        private val characterIdRegex by lazy {
            Regex("https://rickandmortyapi.com/api/character/(?<id>\\d++)$")
        }
        fun String.getCharacterId(): Int = characterIdRegex.matchEntire(this)?.let { matchResult ->
            matchResult.groups["id"]?.value?.toIntOrNull()
        } ?: throw IllegalArgumentException("Can't extract character id from '$this'")
    }
}