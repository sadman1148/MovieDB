package com.techetronventures.moviedb.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun fromListToString(list: List<Int>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun fromStringToList(value: String?): List<Int>? {
        return value?.split(",")?.map { it.toInt() }
    }
}