package com.example.tsogolo.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.tsogolo.model.Career

@Dao
interface CareerDao {

    @Query("SELECT * FROM Career ORDER BY title")
    suspend fun getAll(): List<Career>

    @Query("SELECT Career.*, " +
            "Program.id as pr_id, Program.name, Program.duration " +
            "FROM Career JOIN ProgramCareer ON Career.id = ProgramCareer.careerId" +
            " JOIN Program ON ProgramCareer.programId = Program.id")
    suspend fun careerPrograms(): Map<Career, List<ProgramAlt>>

    @Query("SELECT Career.* FROM Career JOIN PreferredCareer ON Career.id = PreferredCareer.careerId " +
            "WHERE PreferredCareer.userId = :userId ORDER BY PreferredCareer.rank")
    suspend fun getAllOf(userId: Int): List<Career>

    @Query("SELECT Career.*, " +
            "Personality.id as pe_id, Personality.description as pe_description, Personality.type " +
            "FROM Career " +
            "JOIN PersonalitySuitableCareer ON Career.id = PersonalitySuitableCareer.careerId " +
            "JOIN Personality ON PersonalitySuitableCareer.personalityId = Personality.id")
    suspend fun careerPersonalities(): Map<Career, List<PersonalityAlt>>

//    @Transaction
//    @Query("SELECT Career.* FROM Career " +
//            "JOIN CareerCategory ON Career.id = CareerCategory.careerID " +
//            "WHERE CareerCategory.categoryID = :categoryId")
//    suspend fun getCareersByCategory(categoryId: Int): List<Career>



}