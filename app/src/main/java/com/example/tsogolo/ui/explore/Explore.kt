package com.example.tsogolo.ui.explore

import android.content.Intent
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tsogolo.model.Category
import com.example.tsogolo.ui.career.CareerDescriptionActivity
import com.example.tsogolo.ui.components.*
import com.example.tsogolo.ui.theme.Typography
import kotlinx.coroutines.delay


@OptIn(ExperimentalUnitApi::class)
@Composable
fun exploreCareer(
    exploreViewModel: ExploreViewModel = viewModel(),
    backArrowClicked: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        exploreViewModel.initialize(context)
    }

    val categoryList by exploreViewModel.categor
    var showLoader by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000) // Set the waiting time (e.g., 3 seconds)
        showLoader = false
    }

    // New state variables to track search layout visibility and search query
    var showSearchLayout by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.background(color = MaterialTheme.colors.background).fillMaxSize()) {


        if (showSearchLayout) {
            // Display the search row
            TextField(
                value = exploreViewModel.keyword.value,
                onValueChange = { exploreViewModel.onKeywordChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                placeholder = { Text(text = "Search") },
                trailingIcon = {
                        IconButton(onClick = { showSearchLayout = false }) {
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
                title = {
                    if (showSearchLayout) {
                        TextField(
                            value = exploreViewModel.keyword.value,
                            onValueChange = { exploreViewModel.onKeywordChange(it) },
                            placeholder = { Text(text = "Type Keyword") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text("Categories")
                    }
                },
                backgroundColor = MaterialTheme.colors.background,
                contentColor = Color.Black,
                navigationIcon = {
                    if (showSearchLayout) {
                        IconButton(onClick = { showSearchLayout = false }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Cancel",
                                tint = Color.Black
                            )
                        }
                    } else {
                        IconButton(onClick = { backArrowClicked() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back Arrow",
                                tint = Color.Black
                            )
                        }
                    }
                },
                actions = {
                    if (!showSearchLayout) {
                        IconButton(onClick = {
                            showSearchLayout = true
                        }) {
//                            Icon(
//                                imageVector = Icons.Default.Search,
//                                contentDescription = "Search",
//                                tint = Color.Black
//                            )
                        }
                    }
                }
            )
        }



        // Use Crossfade to animate the transition between exploreCareer and CareerSearchLayout
        Crossfade(targetState = showSearchLayout) { isSearching ->
            if (isSearching) {
                // Show CareerSearchLayout when searching
                Column {

                    CareerSearch(
                        exploreViewModel = viewModel(),
                        backArrowClicked = { showSearchLayout = false },
//                        keywords = searchQuery
                    )
                }
            } else {
                if (showLoader) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator() // Show the loader
                    }
                } else {
                    if (categoryList.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = "Categories",
                            style = Typography.subtitle2.copy(
                                color = MaterialTheme.colors.primary,
                                fontFamily = FontFamily.SansSerif
                            )
                        )
                        CareerCategoryList(categoryList) { categoryId ->
                            // Handle category click event here
                            val intent = Intent(context, CategoryDetailsActivity::class.java)
                            intent.putExtra("category_id", categoryId.toInt())
                            context.startActivity(intent)
                        }
                    } else {
                        Text(
                            text = "No categories available",
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CareerSearch(exploreViewModel: ExploreViewModel = viewModel(), backArrowClicked: () -> Unit) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        exploreViewModel.initialize(context)
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {



        Divider(modifier = Modifier.padding(vertical = 8.dp))


        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            val careers = exploreViewModel.careers.value
            Log.d("ExploreViewModel", "Category data retrieveds: $careers")
            if (exploreViewModel.keyword.value.isNotBlank()) {
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


@Composable
fun CareerCategoryList(careerCategories: List<Category>, onCategoryClicked: (String) -> Unit) {
    LazyColumn {
        items(careerCategories) { category ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .clickable { onCategoryClicked(category.id.toString()) },
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
//                    Text(
//                        text = "Category ID: ${category.id}",
//                        style = MaterialTheme.typography.subtitle1
//                    )
                    Text(
                        text = " ${category.categoryName}",
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}





