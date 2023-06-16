package com.example.tsogolo.database

import androidx.room.*
import com.example.tsogolo.model.UserPersonality

@Dao
interface UserPersonalityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userPersonalities: List<UserPersonality>): List<Long>

    @Delete
    suspend fun delete(userPersonality: UserPersonality)

    @Query("SELECT * FROM UserPersonality WHERE userId = :userId ORDER BY rank")
    suspend fun getAllOf(userId: Int): List<UserPersonality>
}
