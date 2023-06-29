package com.example.tsogolo.ui.explore

//import com.example.tsogolo.ui.career.CareerData
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    val filterByGrades: MutableState<Boolean> = mutableStateOf(false)
    val filterByCat: MutableState<Boolean> = mutableStateOf(true)
    val keyword: MutableState<String> = mutableStateOf("")
    val selectedCareers: MutableState<List<Career>> = mutableStateOf(listOf())
    val careers: MutableState<List<CareerData>> = mutableStateOf(listOf())
    val career: MutableState<List<Career>> = mutableStateOf(listOf())
    //    val career: MutableState<List<CareerData>> = mutableStateOf(listOf())
    var selectedCategoryId: Int? by mutableStateOf(null)

//    val careerCategor: MutableState<List<CategoryWithCareer>> = mutableStateOf(listOf())

    private lateinit var db: TsogoloDatabase
    //    private lateinit var db: TsogoloDatabase
    private var _careers: List<Career> = listOf()
    private var _career: List<Career> = listOf()

    var isInitialized: Boolean by mutableStateOf(false) // Flag to track initialization status


    fun initialize(context: Context) {
        db = TsogoloDatabase.getInstance(context)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                var ch = 0
                if(selectedCategoryId != null){
                    _careers = db.careerDao().getCareersByCategory(selectedCategoryId!!)
                }

                val categoryList = db.categoryDao().getAll()
                val careerCategory = db.categoryDao().careerCategor()

                career.value = db.careerDao().getAll()
                _career = db.careerDao().getAll()
                mapCareers()

                categor.value = categoryList
                mapCareers()

                isInitialized = true
//                careerCategor.value = careerCategory
                Log.d("ExploreViewModel", "Category data retrieved: $selectedCategoryId")
            } catch (e: Exception) {
                Log.e("ExploreViewModel", "Failed to retrieve category data: ${e.message}")
            }
        }
    }

    fun reinitialize(context: Context) {
        isInitialized = false
        initialize(context)
    }

    private fun filtered(careers: List<CareerData>): List<CareerData> {

        Log.d("ExploreViewModel", "filtering : $keyword")
        return careers.filter {
            (it.title.contains(keyword.value, true) ||
                    it.description?.contains(keyword.value, true) == true
                    )
        }
    }

//    val filteredCareers: List<CareerData>
//        get() {
//            val selectedId = selectedCategoryId
//            return if (selectedId != null) {
//                careers.value.filter { it.categoryId == selectedId }
//            } else {
//                careers.value
//            }
//        }

//    val filteredCareers: List<CareerData>
//        get() {
//            val selectedId = selectedCategoryId
//            return if (selectedId != null) {
//                careers.value.filter { career ->
//                    // Apply your custom logic to filter based on selectedId
//                    // For example, you can check if the career's title contains the selectedId
//                    career.title.contains(selectedId.toString(), ignoreCase = true)
//                }
//            } else {
//                careers.value
//            }
//        }

    private fun mapCareers() {
        CoroutineScope(Dispatchers.Default).launch {
            this@ExploreViewModel.careers.value = filtered(_career.map { career ->

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

    fun filterByGradesClicked() {
        filterByGrades.value = true; // Update the value of filterByGrades
        filterByCat.value = false;
        mapCareers();
    }

    fun filterByCatClicked() {
        filterByCat.value = true; // Update the value of filterByGrades
        filterByGrades.value = false;
        mapCareers();
    }
}




