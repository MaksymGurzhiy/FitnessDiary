package com.example.fitnessdiary.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.fitnessdiary.data.AppDatabase
import com.example.fitnessdiary.data.model.User
import com.example.fitnessdiary.data.model.UserWeight
import com.example.fitnessdiary.data.model.Workout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = AppDatabase.getDatabase(application)
    private val userDao = database.userDao()
    private val workoutDao = database.workoutDao()
    private val userWeightDao = database.userWeightDao()
    
    private val _currentWeight = MutableLiveData<Float>()
    val currentWeight: LiveData<Float> = _currentWeight
    
    private val _selectedDayOfWeek = MutableLiveData<Int>()
    val selectedDayOfWeek: LiveData<Int> = _selectedDayOfWeek
    
    // All workouts from database
    val allWorkouts: LiveData<List<Workout>> = workoutDao.getAllWorkouts()
    
    // Filtered workouts for the selected day
    val workoutsForSelectedDay: LiveData<List<Workout>> = Transformations.switchMap(selectedDayOfWeek) { day ->
        workoutDao.getWorkoutsByDay(day)
    }
    
    init {
        // Load user weight
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                userDao.getUserSync() ?: User()
            }
            _currentWeight.value = user.currentWeight
            
            // Default to current day of week (1-based where 1=Monday)
            val calendar = Calendar.getInstance()
            val dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1
            _selectedDayOfWeek.value = dayOfWeek
        }
    }
    
    fun updateWeight(weight: Float) {
        updateWeight(weight, Date()) // Use current date as default
    }
    
    fun updateWeight(weight: Float, date: Date) {
        viewModelScope.launch {
            // Update current user weight
            val user = User(currentWeight = weight)
            withContext(Dispatchers.IO) {
                userDao.insertUser(user)
            }
            _currentWeight.value = weight
            
            // Save weight to history
            val weightRecord = UserWeight(
                weight = weight,
                date = date
            )
            withContext(Dispatchers.IO) {
                userWeightDao.insert(weightRecord)
            }
        }
    }
    
    fun selectDay(dayIndex: Int) {
        if (dayIndex in 1..7) {
            _selectedDayOfWeek.value = dayIndex
        }
    }
} 