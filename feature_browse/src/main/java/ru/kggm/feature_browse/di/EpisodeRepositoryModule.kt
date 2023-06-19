package ru.kggm.feature_browse.di

import dagger.Binds
import dagger.Module
import ru.kggm.feature_browse.data.repositories.CharacterRepositoryImpl
import ru.kggm.feature_browse.data.repositories.EpisodeRepositoryImpl
import ru.kggm.feature_browse.domain.repositories.CharacterRepository
import ru.kggm.feature_browse.domain.repositories.EpisodeRepository
import javax.inject.Singleton

@Module
interface EpisodeRepositoryModule {
    @Binds
    @Singleton
    fun provideEpisodeRepository(
        EpisodeRepository: EpisodeRepositoryImpl
    ): EpisodeRepository
}