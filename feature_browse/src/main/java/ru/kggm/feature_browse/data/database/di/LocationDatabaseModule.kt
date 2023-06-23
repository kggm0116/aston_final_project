package ru.kggm.feature_browse.data.database.di

import dagger.Module
import dagger.Provides
import ru.kggm.feature_browse.data.database.SharedDatabase
import ru.kggm.feature_browse.data.database.daos.LocationDao

@Module
object LocationDatabaseModule {
    @Provides
    fun provideLocationDao(database: SharedDatabase): LocationDao = database.locationDao()
}