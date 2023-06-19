package ru.kggm.feature_browse.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate

object LocalDateConverter {
    @TypeConverter
    fun dateToString(value: LocalDate): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun stringToDate(value: String): LocalDate {
        return Gson().fromJson(value, object : TypeToken<LocalDate>() {}.type)
    }
}