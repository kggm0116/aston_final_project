package ru.kggm.feature_browse.data.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.kggm.feature_browse.data.database.SharedDatabase
import ru.kggm.feature_browse.data.database.daos.CharacterDao
import ru.kggm.feature_browse.data.database.daos.EpisodeDao
import ru.kggm.feature_browse.data.database.daos.LocationDao

@Module
object EpisodeDatabaseModule {
    @Provides
    fun provideLEpisodeDao(database: SharedDatabase): EpisodeDao = database.episodeDao()
}