package com.example.tsogolo.database

import androidx.room.*
import com.example.tsogolo.model.PersonalityQuestion

@Dao
interface PersonalityQuestionDao {

    @Query("SELECT * FROM PersonalityQuestion ORDER BY question")
    suspend fun getAll(): List<PersonalityQuestion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: PersonalityQuestion)

    @Update
    suspend fun update(question: PersonalityQuestion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<PersonalityQuestion>)


}