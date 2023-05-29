package ru.kggm.feature_browse.data.network.services

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.kggm.feature_browse.data.network.dtos.entity.EpisodeDto
import ru.kggm.feature_browse.data.network.dtos.page.EpisodePageDto

interface EpisodeService {

    // If nothing matches filters, returns plain error object
    @GET("episode")
    suspend fun getPage(
        @Query("page") pageNumber: Int = 1,
        @Query("name") name: String? = null,
        @Query("code") code: String? = null
    ): EpisodePageDto

    @GET("episode/{ids}")
    suspend fun getById(@Path("ids") id: List<Int>): List<EpisodeDto>

    companion object {
        private val episodeIdRegex by lazy { Regex("https://rickandmortyapi.com/api/episode/(?<id>\\d++)$") }
        fun String.getEpisodeId(): Int = episodeIdRegex.matchEntire(this)?.let { matchResult ->
            matchResult.groups["id"]?.value?.toIntOrNull()
        } ?: throw IllegalArgumentException("Can't extract episode id from '$this'")
    }
}