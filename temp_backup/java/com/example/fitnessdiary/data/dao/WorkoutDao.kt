package com.example.fitnessdiary.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fitnessdiary.data.model.Workout

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout): Long

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    suspend fun getWorkoutById(workoutId: Int): Workout?

    @Query("SELECT * FROM workouts ORDER BY dayOfWeek ASC")
    fun getAllWorkouts(): LiveData<List<Workout>>

    @Query("SELECT * FROM workouts WHERE dayOfWeek = :dayOfWeek")
    fun getWorkoutsByDay(dayOfWeek: Int): LiveData<List<Workout>>

    @Query("SELECT * FROM workouts WHERE date IS NOT NULL ORDER BY date DESC")
    fun getWorkoutHistory(): LiveData<List<Workout>>
} 