package com.example.tsogolo.database

import androidx.room.Dao
import androidx.room.Query
import com.example.tsogolo.model.ProgramRequirement
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramRequirementsDao {
    @Query("SELECT * FROM ProgramRequirement")
    fun getAll(): Flow<List<ProgramRequirement>>
}