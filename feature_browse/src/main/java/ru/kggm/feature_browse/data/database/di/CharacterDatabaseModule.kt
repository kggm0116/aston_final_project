package ru.kggm.feature_browse.data.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.kggm.feature_browse.data.database.SharedDatabase
import ru.kggm.feature_browse.data.database.daos.CharacterDao

@Module
object CharacterDatabaseModule {

    @Provides
    fun provideDatabase(context: Context): SharedDatabase = Room.databaseBuilder(
        context.applicationContext,
        SharedDatabase::class.java,
        SharedDatabase.NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideCharacterDao(database: SharedDatabase): CharacterDao = database.characterDao()
}