package com.example.tsogolo.database

import androidx.room.Dao
import androidx.room.Query
import com.example.tsogolo.model.CareerCategory
import com.example.tsogolo.model.Category
import com.example.tsogolo.model.CategoryWithCareer
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM Category")
    fun getAll(): List<Category>


//    @Query("SELECT Category.* FROM Category " +
//            "JOIN CareerCategory ON Category.id = CareerCategory.categoryID " +
//            "JOIN Career ON CareerCategory.careerID = career.Id ")
//    suspend fun careerCategory(): List<CareerCategory>

    @Query("SELECT Category.* " +
            "FROM Category " +
            "JOIN CareerCategory ON Category.id = CareerCategory.categoryID " +
            "JOIN Career ON CareerCategory.careerID = Career.id " +
            "WHERE CareerCategory.careerID = :careerID")
    suspend fun careerCategor(careerID: Int?): List<CategoryWithCareer>


}