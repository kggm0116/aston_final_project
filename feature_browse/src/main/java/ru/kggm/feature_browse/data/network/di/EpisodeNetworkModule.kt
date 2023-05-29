package ru.kggm.feature_browse.data.network.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.kggm.feature_browse.data.network.services.EpisodeService

@Module
object EpisodeNetworkModule {
    @Provides
    fun provideEpisodeService(
        retrofit: Retrofit
    ): EpisodeService = retrofit.create(EpisodeService::class.java)
}