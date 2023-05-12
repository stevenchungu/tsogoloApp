package com.example.tsogolo.database

import androidx.room.Dao
import androidx.room.Query
import com.example.tsogolo.model.Program
import com.example.tsogolo.model.ProgramRequirement
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramDao {

    @Query("SELECT * FROM Program")
    fun getAll(): Flow<List<Program>>

    @Query("SELECT Program.* FROM Program JOIN ProgramCareer ON Program.id = ProgramCareer.programId " +
            "JOIN PreferredCareer ON ProgramCareer.careerId = PreferredCareer.careerId " +
            "WHERE PreferredCareer.userId = :userId ORDER BY PreferredCareer.rank")
    suspend fun getAllOf(userId: Int): List<Program>

    @Query("SELECT * FROM Program JOIN ProgramRequirement ON Program.id = ProgramRequirement.programId")
    suspend fun programRequirements(): Map<Program, List<ProgramRequirement>>
}