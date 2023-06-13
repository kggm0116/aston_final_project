package ru.kggm.feeature_characters.data.database.daos

import androidx.room.Dao
import ru.kggm.core.data.database.daos.BaseDao
import ru.kggm.feeature_characters.data.entities.CharacterDataEntity

@Dao
interface CharacterDao: BaseDao<CharacterDataEntity> {
}