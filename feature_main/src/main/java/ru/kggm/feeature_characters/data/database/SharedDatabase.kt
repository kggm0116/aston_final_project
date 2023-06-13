package ru.kggm.feeature_characters.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kggm.feeature_characters.data.database.daos.CharacterDao
import ru.kggm.feeature_characters.data.entities.CharacterDataEntity

@Database(
    entities =
    [
        CharacterDataEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class SharedDatabase: RoomDatabase() {
    abstract fun characterDao() : CharacterDao
    companion object {
        const val NAME = "aston_database"
    }
}