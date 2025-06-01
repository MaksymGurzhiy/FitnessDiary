package com.example.fitnessdiary.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: Int = 1, // We'll only have one user
    val currentWeight: Float = 0.0f, // in kg
    val weightHistory: List<WeightEntry> = emptyList()
)

data class WeightEntry(
    val weight: Float, // in kg
    val date: Date
) 