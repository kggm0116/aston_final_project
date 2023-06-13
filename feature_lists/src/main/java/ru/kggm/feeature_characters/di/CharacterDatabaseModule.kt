package ru.kggm.feeature_characters.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.kggm.feeature_characters.data.database.SharedDatabase
import ru.kggm.feeature_characters.data.network.services.CharacterService
import ru.kggm.feeature_characters.data.repositories.CharacterRepositoryImpl
import ru.kggm.feeature_characters.domain.repositories.CharacterRepository
import javax.inject.Singleton

//@Module
//object CharacterDatabaseModule {
//    @Provides
//    fun provideDatabase(context: Context) = Room.databaseBuilder(
//        context.applicationContext,
//        SharedDatabase::class.java,
//        SharedDatabase.NAME
//    )
//        .fallbackToDestructiveMigration()
//        .build()
//
//    @Provides
//    fun provideCharacterDao(database: SharedDatabase) = database.characterDao()
//}