package ru.kggm.feeature_characters.data.network.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.kggm.feeature_characters.data.network.services.CharacterService

@Module
object CharacterNetworkModule {
    @Provides
    fun provideCharacterService(
        retrofit: Retrofit
    ): CharacterService = retrofit.create(CharacterService::class.java)
}