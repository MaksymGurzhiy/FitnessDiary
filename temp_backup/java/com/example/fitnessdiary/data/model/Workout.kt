package com.example.fitnessdiary.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.fitnessdiary.data.model.Exercise
import java.util.Date

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val dayOfWeek: Int, // 1 = Monday, 2 = Tuesday, etc.
    val exercises: List<Exercise> = emptyList(),
    val date: Date? = null // For tracking when a workout was actually performed
)

data class Exercise(
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: Float, // in kg
    val notes: String = ""
) 