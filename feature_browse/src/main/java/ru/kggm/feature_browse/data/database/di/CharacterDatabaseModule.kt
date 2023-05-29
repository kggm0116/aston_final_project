package ru.kggm.feature_browse.data.database.di

import dagger.Module
import dagger.Provides
import ru.kggm.feature_browse.data.database.SharedDatabase
import ru.kggm.feature_browse.data.database.daos.CharacterDao

@Module
object CharacterDatabaseModule {
    @Provides
    fun provideCharacterDao(database: SharedDatabase): CharacterDao = database.characterDao()
}