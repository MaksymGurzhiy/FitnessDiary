package com.example.fitnessdiary.data.utils

import androidx.room.TypeConverter
import com.example.fitnessdiary.data.model.Exercise
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromExerciseList(value: List<Exercise>): String {
        val type = object : TypeToken<List<Exercise>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toExerciseList(value: String): List<Exercise> {
        if (value.isEmpty()) {
            return emptyList()
        }
        val listType = object : TypeToken<List<Exercise>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
} 