package com.example.fitnessdiary.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fitnessdiary.data.model.UserWeight

@Dao
interface UserWeightDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userWeight: UserWeight)
    
    @Query("SELECT * FROM user_weights ORDER BY date DESC LIMIT 1")
    suspend fun getLatestWeight(): UserWeight?
    
    @Query("SELECT * FROM user_weights ORDER BY date DESC")
    suspend fun getAllWeights(): List<UserWeight>

    @Query("SELECT * FROM user_weights ORDER BY date DESC")
    fun getAllWeightsLive(): LiveData<List<UserWeight>>
} 