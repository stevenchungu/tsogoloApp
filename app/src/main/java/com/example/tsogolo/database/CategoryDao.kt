package com.example.tsogolo.database

import android.util.Log
import androidx.room.Dao
import androidx.room.Query
import androidx.sqlite.db.SimpleSQLiteQuery
//import com.example.tsogolo.model.CareerCategories
import com.example.tsogolo.model.CareerCategory
//import com.example.tsogolo.model.CareerCategory
import com.example.tsogolo.model.Category
//import com.example.tsogolo.model.CategoryWithCareer
import com.example.tsogolo.ui.explore.ExploreViewModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM Category")
    fun getAll(): List<Category>

    @Query("SELECT * FROM CareerCategory")
    fun careerCategor(): List<CareerCategory>

    @Query("SELECT Category.* FROM Category JOIN CareerCategory ON Category.id = CareerCategory.categoryId JOIN Career ON CareerCategory.careerId = career.id")
    suspend fun careerCategory(): List<Category>



//    @Query("SELECT Category.* " +
//            "FROM Category " +
//            "JOIN CareerCategory ON Category.id = CareerCategory.categoryID " +
//            "JOIN Career ON CareerCategory.careerID = Career.id " +
//            "WHERE CareerCategory.careerID = :careerID")
//    suspend fun careerCategor(careerID: Int?): List<CategoryWithCareer>

//    @Query("SELECT Category.* " +
//            "FROM Category " +
//            "JOIN CareerCategory ON Category.id = CareerCategory.categoryID " +
//            "JOIN Career ON CareerCategory.careerID = Career.id " +
//            "WHERE CareerCategory.careerID = :careerID")
//    suspend fun careerCategor(careerID: Int?) {
//        val queryString = "SELECT Category.* " +
//                "FROM Category " +
//                "JOIN CareerCategory ON Category.id = CareerCategory.categoryID " +
//                "JOIN Career ON CareerCategory.careerID = Career.id " +
//                "WHERE CareerCategory.careerID = :careerID"
//        val parameters = arrayOf(careerID)
//        Log.d("YourDao", "Executing query: $queryString with parameters: ${parameters.contentToString()}")
//
//    }





}