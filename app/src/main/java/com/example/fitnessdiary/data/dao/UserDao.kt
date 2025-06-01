package com.example.fitnessdiary.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fitnessdiary.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE id = 1")
    fun getUser(): LiveData<User>

    @Query("SELECT * FROM users WHERE id = 1")
    suspend fun getUserSync(): User?

    @Query("UPDATE users SET currentWeight = :weight WHERE id = 1")
    suspend fun updateUserWeight(weight: Float)
} 