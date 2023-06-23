package ru.kggm.feature_browse.di

import dagger.Binds
import dagger.Module
import ru.kggm.feature_browse.data.repositories.LocationRepositoryImpl
import ru.kggm.feature_browse.domain.repositories.LocationRepository
import javax.inject.Singleton

@Module
interface LocationRepositoryModule {
    @Binds
    @Singleton
    fun provideLocationRepository(
        locationRepository: LocationRepositoryImpl
    ): LocationRepository
}