package com.example.tsogolo.ui.career

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tsogolo.database.PersonalityAlt
import com.example.tsogolo.database.ProgramAlt
import com.example.tsogolo.database.TsogoloDatabase
import com.example.tsogolo.model.*
import com.example.tsogolo.ui.components.CareerItem
import com.example.tsogolo.ui.components.SearchItem
import com.example.tsogolo.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CareerSearchLayout(
        careerSearchViewModel: CareerSearchViewModel = viewModel(),
        backArrowClicked: () -> Unit,
//        keywords: MutableState<String>
    ) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        careerSearchViewModel.initialize(context)
    }

        Column(modifier = Modifier.background(color = MaterialTheme.colors.background)
            .fillMaxSize()) {
            TopAppBar(
                title = { Text("Search for Career") },
                navigationIcon = { Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Arrow",
                    Modifier.clickable { backArrowClicked() },
//                tint = MaterialTheme.colors.primary
                )
                },
                backgroundColor = MaterialTheme.colors.background,
//            contentColor = MaterialTheme.colors.primary
            )

            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {

//                    careerSearchViewModel.keyword = keywords
                    OutlinedTextField(
                        value = careerSearchViewModel.keyword.value,
                        onValueChange = { careerSearchViewModel.onKeywordChange(it) },
                        placeholder = { Text(text = "Type Keyword") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = Typography.body1.copy(color = MaterialTheme.colors.onSurface)
                    )


                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))


                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    val careers = careerSearchViewModel.careers.value
                    Log.d("ExploreViewModel", "Category data retrieved: $careers")

                    careers.forEach { careerData ->
                        SearchItem(career = careerData, onClick = {
                            Log.d("CategoryViewModel", "tione: ${careerData.id}")
                            val intent = Intent(context, CareerDescriptionActivity::class.java).apply {
                                putExtra("careerId", careerData.id)
                            }
                            context.startActivity(intent)
                        })
                        Divider()
                    }
                }


            }
        }
    }


class CareerSearchViewModel : ViewModel() {

    val keyword: MutableState<String> = mutableStateOf("")
    val careers: MutableState<List<CareerData>> = mutableStateOf(listOf())

    private var _careers: List<Career> = listOf()

    private lateinit var db: TsogoloDatabase

    fun initialize(
        context: Context
    ) {
        db = TsogoloDatabase.getInstance(context = context)

        CoroutineScope(Dispatchers.IO).launch {
            _careers = db.careerDao().getAll()
            println("Retrieved careers: $_careers")
            Log.i("CareerSearchViewModel", "Retrieved careers: $_careers")
//            mapCareers()
        }
        println("Retrieved careers: $_careers")
        Log.i("CareerSearchViewModel", "Retrieved careers: $_careers")
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
            this@CareerSearchViewModel.careers.value = filtered(_careers.map { career ->
                CareerData(
                    id = career.id!!,
                    title = career.title ?: "Unknown",
                    aas = career.aas,
                    description = career.description
                )
            })
            Log.d("ExploreViewModel", "launcher : $keyword")
        }
    }


    fun onKeywordChange(keyword: String) {
        this.keyword.value = keyword
        mapCareers()
    }

}
