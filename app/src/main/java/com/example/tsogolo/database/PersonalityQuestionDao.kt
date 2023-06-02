package com.example.tsogolo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tsogolo.model.PersonalityQuestion

@Dao
interface PersonalityQuestionDao {

    @Query("SELECT * FROM PersonalityQuestion ORDER BY question")
    suspend fun getAll(): List<PersonalityQuestion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(personalityQuestion: List<PersonalityQuestion>)
}