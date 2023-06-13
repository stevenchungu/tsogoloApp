package com.example.tsogolo.ui.explore

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tsogolo.database.TsogoloDatabase
import com.example.tsogolo.model.Career
import com.example.tsogolo.ui.career.CareerData
import com.example.tsogolo.ui.career.CareerDescriptionActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalUnitApi::class)
@Composable
fun categoryDetail(
    categoryViewModel: CategoryViewModel = viewModel(),
    categoryId: Int,
    backArrowClicked: () -> Unit
) {
    val context = LocalContext.current
    remember { mutableStateOf("") }
    val searchActive = remember { mutableStateOf(false) }
    var showLoader by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        categoryViewModel.initialize(context, categoryId)
        delay(1500) // Set the waiting time (e.g., 1 seconds)
        showLoader = false
    }

    val careers = categoryViewModel.career.value
    val isLoading = categoryViewModel.isLoading.value

    Column(modifier = Modifier.background(color = MaterialTheme.colors.background).fillMaxSize()) {

        if (searchActive.value) {
            // Display the search row
            TextField(
                value = categoryViewModel.keyword.value,
                onValueChange = { categoryViewModel.onKeywordChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text(text = "Search") },
                trailingIcon = {
                    IconButton(onClick = { searchActive.value = false }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Cancel",
                            tint = Color.Black
                        )
                    }
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface),
                textStyle = MaterialTheme.typography.body1
            )
        } else {

            TopAppBar(

                title = { Text("Career") },
                backgroundColor = MaterialTheme.colors.background,
                contentColor = Color.Black,
                navigationIcon = {
                    IconButton(onClick = { backArrowClicked() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Arrow",
                            tint = Color.Black
                        )
                    }
                },


                actions = {
                        // Display the search icon
                        IconButton(onClick = { searchActive.value = true }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Black
                            )
                        }



                }
            )

        }

        Column(modifier = Modifier.padding(vertical = 2.dp).shadow(2.dp)) {
            if (showLoader) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator() // Show the loader
                }
            } else {
                if (careers.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(careers) { career ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp)
                                    .clickable {
                                        career.id.let {
                                            Log.d("CategoryViewModel", "tione: ${career.id}")
                                            val intent = Intent(context, CareerDescriptionActivity::class.java).apply {
                                                putExtra("careerId", career.id)
                                            }
                                            context.startActivity(intent)
                                        }
                                    },
                                elevation = 4.dp
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {
                                    // Display career details here inside the card
                                    Text(text = career.title)
                                }
                            }
                        }
                    }
                } else {
                    Text(text = "No careers available in this category")
                }
            }
        }
    }
}



class CategoryViewModel : ViewModel() {
    val careers: MutableState<List<Career>> = mutableStateOf(listOf())
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val filteredCareers: MutableState<List<Career>> = mutableStateOf(listOf())
    val keyword: MutableState<String> = mutableStateOf("")
    val selectedCareers: MutableState<List<Career>> = mutableStateOf(listOf())
    val career: MutableState<List<CareerData>> = mutableStateOf(listOf())
    private var _careers: List<Career> = listOf()


    private lateinit var db: TsogoloDatabase

    fun initialize(context: Context, categoryId: Int) {
        isLoading.value = true
        db = TsogoloDatabase.getInstance(context)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val careersByCategory = db.careerDao().getCareersByCategory(categoryId)
                careers.value = careersByCategory
                filteredCareers.value = careersByCategory
                isLoading.value = false
                _careers = db.careerDao().getCareersByCategory(categoryId)
                mapCareers()
                Log.d("CategoryViewModel", "Careers retrieved id: $categoryId")
                Log.d("CategoryViewModel", "Careers retrieved: $_careers")
            } catch (e: Exception) {
                isLoading.value = false
                Log.e("CategoryViewModel", "Failed to retrieve careers: ${e.message}")
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
            this@CategoryViewModel.career.value = filtered(_careers.map { career ->
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



@Composable
private fun OpenCareerDescriptionActivity(careerId: Int) {
    val context = LocalContext.current
    val intent = Intent(context, CareerDescriptionActivity::class.java).apply {
        putExtra("careerId", careerId)
    }
    context.startActivity(intent)
}

