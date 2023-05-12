package com.example.tsogolo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tsogolo.model.Subject
import com.example.tsogolo.model.UserSubject
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {

    @Query("SELECT * FROM Subject")
    fun getAll(): Flow<List<Subject>>

    @Insert
    suspend fun insertAll(userSubjects: List<Subject>): List<Long>

    @Query("SELECT Subject.* FROM Subject " +
            "JOIN UserSubject ON Subject.id = UserSubject.subjectId " +
            "WHERE UserSubject.userId = :userId ORDER BY UserSubject.grade")
    suspend fun getAllOf(userId: Int): List<Subject>

    @Query("SELECT DISTINCT Subject.* FROM Subject " +
            "JOIN ProgramRequirement ON Subject.id = ProgramRequirement.subjectId " +
            "JOIN ProgramCareer ON ProgramRequirement.programId = ProgramCareer.programId " +
            "JOIN PreferredCareer ON ProgramCareer.careerId = PreferredCareer.careerId " +
            "WHERE PreferredCareer.userId = :userId ORDER BY PreferredCareer.rank")
    suspend fun getAllOfFromCareers(userId: Int): List<Subject>
}