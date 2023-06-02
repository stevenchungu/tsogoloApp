package com.example.tsogolo.ui.explore

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tsogolo.database.TsogoloDatabase
import com.example.tsogolo.model.Career
import com.example.tsogolo.model.Category
import com.example.tsogolo.ui.career.CareerDescriptionActivity
import com.example.tsogolo.ui.career.CareerSearchActivity
import com.example.tsogolo.ui.components.SearchItem
import com.example.tsogolo.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalUnitApi::class)
@Composable
fun categoryDetail(
    categoryViewModel: CategoryViewModel = viewModel(),
    categoryId: Int,
    backArrowClicked: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        categoryViewModel.initialize(context, categoryId)
    }

    val careers = categoryViewModel.careers.value

    Column(modifier = Modifier.background(color = MaterialTheme.colors.background).fillMaxSize()) {
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
                IconButton(onClick = {
                    context.startActivity(Intent(context, CareerSearchActivity::class.java))
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Black
                    )
                }
            }
        )

        Column(modifier = Modifier.padding(horizontal = 8.dp).shadow(2.dp)) {
            if (careers.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(careers) { career ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                            elevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable { career.id?.let {
                                        Log.d("CategoryViewModel", "tione: ${career.id}")
                                        val intent = Intent(context, CareerDescriptionActivity::class.java).apply {
                                            putExtra("careerId", career.id)
                                        }
                                        context.startActivity(intent)
                                    } }, // Add clickable modifier and onClick callback
                            ) {
                                // Display career details here inside the card
                                Text(text = career.title.toString())
//                                Divider()
                            }
                        }
                    }
                }
            } else {
                Text(text = "No careers available")
            }
        }

    }
}

@Composable
private fun openCareerDescriptionActivity(careerId: Int) {
    val context = LocalContext.current
    val intent = Intent(context, CareerDescriptionActivity::class.java).apply {
        putExtra("careerId", careerId)
    }
    context.startActivity(intent)
}

class CategoryViewModel : ViewModel() {
    val careers: MutableState<List<Career>> = mutableStateOf(listOf())

    private lateinit var db: TsogoloDatabase

    fun initialize(context: Context, categoryId: Int) {
        db = TsogoloDatabase.getInstance(context)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val careersByCategory = db.careerDao().getCareersByCategory(categoryId)
                careers.value = careersByCategory
                Log.d("CategoryViewModel", "Careers retrieved id: $categoryId")
                Log.d("CategoryViewModel", "Careers retrieved: $careersByCategory")
            } catch (e: Exception) {
                Log.e("CategoryViewModel", "Failed to retrieve careers: ${e.message}")
            }
        }
    }


}
