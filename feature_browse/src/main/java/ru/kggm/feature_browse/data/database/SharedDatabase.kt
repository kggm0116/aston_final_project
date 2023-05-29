package ru.kggm.feature_browse.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.kggm.feature_browse.data.database.converters.IntListConverter
import ru.kggm.feature_browse.data.database.daos.CharacterDao
import ru.kggm.feature_browse.data.database.daos.EpisodeDao
import ru.kggm.feature_browse.data.database.daos.LocationDao
import ru.kggm.feature_browse.data.entities.CharacterDataEntity
import ru.kggm.feature_browse.data.entities.EpisodeDataEntity
import ru.kggm.feature_browse.data.entities.LocationDataEntity

@Database(
    entities =
    [
        CharacterDataEntity::class,
        LocationDataEntity::class,
        EpisodeDataEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    IntListConverter::class,
)
abstract class SharedDatabase: RoomDatabase() {

    abstract fun characterDao() : CharacterDao
    abstract fun locationDao() : LocationDao
    abstract fun episodeDao() : EpisodeDao
    
    companion object {
        const val NAME = "aston_database"
        const val TABLE_CHARACTER = "table_character"
        const val TABLE_LOCATION = "table_location"
        const val TABLE_EPISODE = "table_episode"
    }
}