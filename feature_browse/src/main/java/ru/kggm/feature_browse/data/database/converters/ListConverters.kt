package ru.kggm.feature_browse.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

abstract class ListConverter<T: Serializable> {
    @TypeConverter
    fun intListToString(value: List<T>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun stringToIntList(value: String): List<T> {
        return Gson().fromJson(value, object : TypeToken<List<T>>() {}.type)
    }
}

object IntListConverter : ListConverter<Int>()