package ru.kggm.feature_browse.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kggm.feature_browse.data.database.daos.CharacterDao
import ru.kggm.feature_browse.data.entities.CharacterDataEntity

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
        const val TABLE_CHARACTER = "character_table"
    }
}