package ru.kggm.feature_browse.data.database.di

import dagger.Module
import dagger.Provides
import ru.kggm.feature_browse.data.database.SharedDatabase
import ru.kggm.feature_browse.data.database.daos.EpisodeDao

@Module
object EpisodeDatabaseModule {
    @Provides
    fun provideLEpisodeDao(database: SharedDatabase): EpisodeDao = database.episodeDao()
}