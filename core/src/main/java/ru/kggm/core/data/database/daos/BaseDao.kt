package ru.kggm.core.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface BaseDao<T> {
    @Insert
    suspend fun add(item: T)
    @Insert
    suspend fun add(items: List<T>)
    @Delete
    suspend fun delete(item: T)
    @Delete
    suspend fun delete(items: List<T>)
    @Update
    suspend fun update(item: T)
    @Update
    suspend fun update(items: List<T>)
}