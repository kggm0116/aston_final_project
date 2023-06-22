package ru.kggm.feature_browse.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

object IntListConverter {
    @TypeConverter
    fun intListToString(value: List<Int>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun stringToIntList(value: String?): List<Int>? {
        return Gson().fromJson(value, object : TypeToken<List<Int>>() {}.type)
    }
}