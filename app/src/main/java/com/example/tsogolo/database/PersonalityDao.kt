package com.example.tsogolo.database

import androidx.room.Dao
import androidx.room.Query
import com.example.tsogolo.model.Personality
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalityDao {

    @Query("SELECT * FROM Personality")
    suspend fun getAll(): List<Personality>

    @Query("SELECT Personality.* FROM Personality JOIN UserPersonality ON Personality.id = UserPersonality.personalityId " +
            "WHERE UserPersonality.userId = :userId ORDER BY UserPersonality.rank")
    suspend fun getAllOf(userId: Int): List<Personality>

    @Query("SELECT * FROM Personality WHERE id = :personalityId LIMIT 1")
    suspend fun getById(personalityId: Int): Personality
}