package com.example.tsogolo.database

import androidx.room.*
import com.example.tsogolo.model.UserSubject
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSubjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userSubjects: List<UserSubject>): List<Long>

    @Delete
    suspend fun delete(userSubject: UserSubject)

    @Query("SELECT * FROM UserSubject WHERE userId = :userId")
    suspend fun allOf(userId: Int): List<UserSubject>
}