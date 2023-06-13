package com.example.tsogolo.ui.explore

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tsogolo.model.Category
import com.example.tsogolo.ui.components.*
import kotlinx.coroutines.delay


@OptIn(ExperimentalUnitApi::class)
@Composable
fun exploreCareer(
    exploreViewModel: ExploreViewModel = viewModel(),
    backArrowClicked: () -> Unit
) {
    val context = LocalContext.current
    val categoryList by exploreViewModel.categor
    var showLoader by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        exploreViewModel.initialize(context)
        delay(1000) // Set the waiting time (e.g., 1 seconds)
        showLoader = false
    }

    Column(modifier = Modifier.background(color = MaterialTheme.colors.background).fillMaxSize()) {


            TopAppBar(
                title = {

                        Text("Categories")

                },
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

                }

            )


                if (showLoader) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator() // Show the loader
                    }
                } else {
                    if (categoryList.isNotEmpty()) {

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





