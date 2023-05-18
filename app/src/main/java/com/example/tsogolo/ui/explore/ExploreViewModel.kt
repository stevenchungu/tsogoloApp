package com.example.tsogolo.ui.explore

import android.content.Context
import android.util.Log
import androidx.compose.compiler.plugins.kotlin.inference.Item
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tsogolo.database.CareerDao
import com.example.tsogolo.database.PersonalityAlt
import com.example.tsogolo.database.ProgramAlt
import com.example.tsogolo.database.TsogoloDatabase
import com.example.tsogolo.model.*
import com.example.tsogolo.ui.career.CareerData
//import com.example.tsogolo.ui.career.CareerData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class ExploreViewModel : ViewModel() {
    val categor: MutableState<List<Category>> = mutableStateOf(listOf())
    val careerCategor: MutableState<List<CategoryWithCareer>> = mutableStateOf(listOf())

    private lateinit var db: TsogoloDatabase

    fun initialize(context: Context) {
        db = TsogoloDatabase.getInstance(context)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val categoryList = db.categoryDao().getAll()
                val careerCategory = db.categoryDao().careerCategor(1)
                categor.value = categoryList
                careerCategor.value = careerCategory
                Log.d("ExploreViewModel", "Category data retrieved: $careerCategory")
            } catch (e: Exception) {
                Log.e("ExploreViewModel", "Failed to retrieve category data: ${e.message}")
            }
        }
    }
}




