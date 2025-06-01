package com.example.fitnessdiary.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: Int = 1, // We'll only have one user
    val currentWeight: Float = 0.0f // in kg
) 