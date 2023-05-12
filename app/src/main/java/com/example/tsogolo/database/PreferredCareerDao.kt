package com.example.tsogolo.database

import androidx.room.*
import com.example.tsogolo.model.PreferredCareer
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferredCareerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(prefCareers: List<PreferredCareer>): List<Long>

    @Delete
    suspend fun delete(prefCareer: PreferredCareer)

    @Query("SELECT * FROM PreferredCareer WHERE userId = :userId")
    fun getAllOf(userId: Int): Flow<List<PreferredCareer>>

    @Query("SELECT * FROM PreferredCareer WHERE userId = :userId")
    fun getAllOfList(userId: Int): List<PreferredCareer>
}