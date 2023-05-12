package com.example.tsogolo.database

import androidx.room.*
import com.example.tsogolo.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM User")
    fun getAll(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE id = :userId LIMIT 1")
    suspend fun getById(userId: Int): User

    @Update
    fun update(user: User)
}