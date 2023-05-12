package com.example.tsogolo.database

import androidx.room.Dao
import androidx.room.Query
import com.example.tsogolo.model.PersonalitySuitableCareer
import kotlinx.coroutines.flow.Flow

@Dao
interface PscDao {

    @Query("SELECT * FROM PersonalitySuitableCareer")
    suspend fun getAll(): List<PersonalitySuitableCareer>
}