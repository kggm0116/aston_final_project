package ru.kggm.feature_browse.data.network.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.kggm.feature_browse.data.network.services.LocationService

@Module
object LocationNetworkModule {
    @Provides
    fun provideLocationService(
        retrofit: Retrofit
    ): LocationService = retrofit.create(LocationService::class.java)
}