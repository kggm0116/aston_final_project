package ru.kggm.feature_browse.di

import dagger.Binds
import dagger.Module
import ru.kggm.feature_browse.data.repositories.CharacterRepositoryImpl
import ru.kggm.feature_browse.domain.repositories.CharacterRepository
import javax.inject.Singleton

@Module
interface CharacterRepositoryModule {
    @Binds
    @Singleton
    fun provideCharacterRepository(
        characterRepository: CharacterRepositoryImpl
    ): CharacterRepository
}