package ru.kggm.feeature_characters.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.kggm.feeature_characters.data.network.services.CharacterService
import ru.kggm.feeature_characters.data.repositories.CharacterRepositoryImpl
import ru.kggm.feeature_characters.domain.repositories.CharacterRepository
import javax.inject.Singleton

@Module
interface CharacterRepositoryModule {
    @Binds
    @Singleton
    fun provideCharacterRepository(
        characterRepository: CharacterRepositoryImpl
    ): CharacterRepository
}