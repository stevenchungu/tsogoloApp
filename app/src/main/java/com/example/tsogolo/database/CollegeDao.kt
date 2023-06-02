package com.example.tsogolo.database

import androidx.room.Dao
import androidx.room.Query
import com.example.tsogolo.model.College
import kotlinx.coroutines.flow.Flow

@Dao
interface CollegeDao {

    @Query("SELECT * FROM College")
    fun getAll(): Flow<List<College>>

    @Query("SELECT College.* FROM College " +
            "JOIN CollegeProgram ON College.id = CollegeProgram.collegeId " +
            "JOIN ProgramCareer ON CollegeProgram.programId = ProgramCareer.programId " +
            "JOIN PreferredCareer ON ProgramCareer.careerId = PreferredCareer.careerId " +
            "WHERE PreferredCareer.userId = :userId ORDER BY PreferredCareer.rank")
    suspend fun getAllOfUser(userId: Int): List<College>

    @Query("SELECT * FROM College " +
            "JOIN CollegeProgram ON College.id = CollegeProgram.collegeId " +
            "WHERE CollegeProgram.programId = :programId")
    suspend fun getAllProgram(programId: Int?): List<College>

    @Query("SELECT * FROM College " +
            "JOIN CollegeProgram ON College.id = CollegeProgram.collegeId " +
            "JOIN ProgramCareer ON CollegeProgram.programId = ProgramCareer.programId " +
            "WHERE ProgramCareer.careerId = :careerId")
    suspend fun getAllCollege(careerId: Int?): List<College>

}