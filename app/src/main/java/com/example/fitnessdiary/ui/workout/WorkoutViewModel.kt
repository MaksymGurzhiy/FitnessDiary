package com.example.fitnessdiary.ui.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fitnessdiary.data.AppDatabase
import com.example.fitnessdiary.data.model.Exercise
import com.example.fitnessdiary.data.model.Workout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = AppDatabase.getDatabase(application)
    private val workoutDao = database.workoutDao()
    
    private val _workoutSaved = MutableLiveData<Boolean>()
    val workoutSaved: LiveData<Boolean> = _workoutSaved
    
    private val _workoutDeleted = MutableLiveData<Boolean>()
    val workoutDeleted: LiveData<Boolean> = _workoutDeleted
    
    // Get all workouts
    val allWorkouts: LiveData<List<Workout>> = workoutDao.getAllWorkouts()
    
    fun getWorkout(workoutId: Int): LiveData<Workout?> {
        val result = MutableLiveData<Workout?>()
        
        if (workoutId <= 0) {
            result.value = null
            return result
        }
        
        viewModelScope.launch {
            val workout = withContext(Dispatchers.IO) {
                workoutDao.getWorkoutById(workoutId)
            }
            result.value = workout
        }
        
        return result
    }
    
    fun saveWorkout(name: String, dayOfWeek: Int, exercises: List<Exercise>) {
        if (name.isBlank() || exercises.isEmpty()) return
        
        val workout = Workout(
            name = name,
            dayOfWeek = dayOfWeek,
            exercises = exercises
        )
        
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                workoutDao.insertWorkout(workout)
            }
            _workoutSaved.value = true
        }
    }
    
    fun updateWorkout(id: Int, name: String, dayOfWeek: Int, exercises: List<Exercise>) {
        if (name.isBlank() || exercises.isEmpty()) return
        
        val workout = Workout(
            id = id,
            name = name,
            dayOfWeek = dayOfWeek,
            exercises = exercises
        )
        
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                workoutDao.updateWorkout(workout)
            }
            _workoutSaved.value = true
        }
    }
    
    fun resetSavedState() {
        _workoutSaved.value = false
    }
    
    fun resetDeletedState() {
        _workoutDeleted.value = false
    }
    
    fun deleteWorkout(workoutId: Int) {
        if (workoutId <= 0) return
        
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val workout = workoutDao.getWorkoutById(workoutId)
                workout?.let {
                    workoutDao.deleteWorkout(it)
                }
            }
            _workoutDeleted.value = true
        }
    }
    
    fun loadWorkouts() {
        // This method is kept for consistency
        // The LiveData from workoutDao will automatically update when data changes
    }
} 