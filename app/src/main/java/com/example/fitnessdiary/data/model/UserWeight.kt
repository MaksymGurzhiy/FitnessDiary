package com.example.fitnessdiary.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "user_weights")
data class UserWeight(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val weight: Float,
    val date: Date
) 