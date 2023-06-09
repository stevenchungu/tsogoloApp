package com.example.tsogolo.ui.explore

//import com.example.tsogolo.ui.career.CareerData
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tsogolo.database.TsogoloDatabase
import com.example.tsogolo.model.Career
import com.example.tsogolo.model.Category
import com.example.tsogolo.ui.career.CareerData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExploreViewModel : ViewModel() {
    val categor: MutableState<List<Category>> = mutableStateOf(listOf())
    val keyword: MutableState<String> = mutableStateOf("")
    val selectedCareers: MutableState<List<Career>> = mutableStateOf(listOf())
    val careers: MutableState<List<CareerData>> = mutableStateOf(listOf())

//    val careerCategor: MutableState<List<CategoryWithCareer>> = mutableStateOf(listOf())

    private lateinit var db: TsogoloDatabase
//    private lateinit var db: TsogoloDatabase
    private var _careers: List<Career> = listOf()

    fun initialize(context: Context) {
        db = TsogoloDatabase.getInstance(context)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                _careers = db.careerDao().getAll()
                val categoryList = db.categoryDao().getAll()
                val careerCategory = db.categoryDao().careerCategor()
                categor.value = categoryList
                mapCareers()
//                careerCategor.value = careerCategory
                Log.d("ExploreViewModel", "Category data retrieved: $careerCategory")
            } catch (e: Exception) {
                Log.e("ExploreViewModel", "Failed to retrieve category data: ${e.message}")
            }
        }
    }

    private fun filtered(careers: List<CareerData>): List<CareerData> {

        Log.d("ExploreViewModel", "filtering : $keyword")
        return careers.filter {
            (it.title.contains(keyword.value, true) ||
                    it.description?.contains(keyword.value, true) == true
                    )
        }
    }

    private fun mapCareers() {
        CoroutineScope(Dispatchers.Default).launch {
            this@ExploreViewModel.careers.value = filtered(_careers.map { career ->
                CareerData(
                    id = career.id!!,
                    title = career.title ?: "Unknown",
                    aas = career.aas,
                    description = career.description
                )
            })
            Log.d("ExploreViewModel", "launcher: $keyword")
        }
    }


    fun onKeywordChange(keyword: String) {
        this.keyword.value = keyword
        Log.d("ExploreViewModel", "Changed: $keyword")
        // Perform filtering and update the careers list
        mapCareers()
    }
}




