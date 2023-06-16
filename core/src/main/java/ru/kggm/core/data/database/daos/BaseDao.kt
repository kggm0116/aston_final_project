package ru.kggm.core.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: T)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(items: List<T>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(item: T)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(items: List<T>)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertOrAbort(item: T)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertOrAbort(items: List<T>)

    @Delete
    suspend fun delete(item: T)
    @Delete
    suspend fun delete(items: List<T>)
    @Update
    suspend fun update(item: T)
    @Update
    suspend fun update(items: List<T>)
}