package com.example.fitnessdiary.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.fitnessdiary.data.AppDatabase
import com.example.fitnessdiary.data.model.UserWeight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = AppDatabase.getDatabase(application)
    private val userWeightDao = database.userWeightDao()
    
    // Raw data from database
    private val rawWeightHistory: LiveData<List<UserWeight>> = userWeightDao.getAllWeightsLive()
    
    // Sort order enums
    enum class DateSortOrder { DATE_DESC, DATE_ASC }
    enum class WeightSortOrder { NONE, WEIGHT_ASC, WEIGHT_DESC }
    
    private val _currentDateSortOrder = MutableLiveData<DateSortOrder>(DateSortOrder.DATE_DESC)
    val currentDateSortOrder: LiveData<DateSortOrder> = _currentDateSortOrder
    
    private val _currentWeightSortOrder = MutableLiveData<WeightSortOrder>(WeightSortOrder.NONE)
    val currentWeightSortOrder: LiveData<WeightSortOrder> = _currentWeightSortOrder
    
    // Sorted weight history
    private val _weightHistory = MediatorLiveData<List<UserWeight>>()
    val weightHistory: LiveData<List<UserWeight>> = _weightHistory
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    init {
        _isLoading.value = false
        
        // Add raw data source
        _weightHistory.addSource(rawWeightHistory) { data ->
            applySorting(data)
        }
        
        // Add date sort order source
        _weightHistory.addSource(_currentDateSortOrder) { _ ->
            rawWeightHistory.value?.let { data ->
                applySorting(data)
            }
        }
        
        // Add weight sort order source
        _weightHistory.addSource(_currentWeightSortOrder) { _ ->
            rawWeightHistory.value?.let { data ->
                applySorting(data)
            }
        }
    }
    
    private fun applySorting(data: List<UserWeight>) {
        val weightSortOrder = _currentWeightSortOrder.value ?: WeightSortOrder.NONE
        val dateSortOrder = _currentDateSortOrder.value ?: DateSortOrder.DATE_DESC
        
        // First sort by the active sort criterion
        val sortedList = when {
            weightSortOrder != WeightSortOrder.NONE -> {
                when (weightSortOrder) {
                    WeightSortOrder.WEIGHT_ASC -> data.sortedBy { it.weight }
                    WeightSortOrder.WEIGHT_DESC -> data.sortedByDescending { it.weight }
                    else -> data // Should not happen
                }
            }
            else -> {
                when (dateSortOrder) {
                    DateSortOrder.DATE_DESC -> data.sortedByDescending { it.date }
                    DateSortOrder.DATE_ASC -> data.sortedBy { it.date }
                }
            }
        }
        
        _weightHistory.value = sortedList
    }
    
    fun toggleWeightSort() {
        val currentOrder = _currentWeightSortOrder.value ?: WeightSortOrder.NONE
        
        _currentWeightSortOrder.value = when (currentOrder) {
            WeightSortOrder.NONE -> WeightSortOrder.WEIGHT_ASC
            WeightSortOrder.WEIGHT_ASC -> WeightSortOrder.WEIGHT_DESC
            WeightSortOrder.WEIGHT_DESC -> WeightSortOrder.NONE
        }
        
        // If we're not sorting by weight, ensure date sorting is active
        if (_currentWeightSortOrder.value == WeightSortOrder.NONE) {
            _currentDateSortOrder.value = DateSortOrder.DATE_DESC
        }
    }
    
    fun toggleDateSort() {
        val currentOrder = _currentDateSortOrder.value ?: DateSortOrder.DATE_DESC
        
        _currentDateSortOrder.value = when (currentOrder) {
            DateSortOrder.DATE_DESC -> DateSortOrder.DATE_ASC
            DateSortOrder.DATE_ASC -> DateSortOrder.DATE_DESC
        }
        
        // When sorting by date, disable weight sorting
        _currentWeightSortOrder.value = WeightSortOrder.NONE
    }
} 