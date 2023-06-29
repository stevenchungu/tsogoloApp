@file:OptIn(ExperimentalMaterialApi::class)

package com.example.tsogolo.ui.explore

import android.content.Intent
import android.util.Log
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
import com.example.tsogolo.ui.career.CareerDescriptionActivity
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
    val searchActive = remember { mutableStateOf(false) }
    var showLoader by remember { mutableStateOf(true) }
    val careers = exploreViewModel.careers.value

    LaunchedEffect(Unit) {
        exploreViewModel.initialize(context)
        delay(1000) // Set the waiting time (e.g., 1 seconds)
        showLoader = false
    }

    Column(modifier = Modifier
        .background(color = MaterialTheme.colors.background)
        .fillMaxSize()) {

        if (searchActive.value) {
            // Display the search row
            TextField(
                value = exploreViewModel.keyword.value,
                onValueChange = { exploreViewModel.onKeywordChange(it) },
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
        Divider(modifier = Modifier.padding(vertical = 4.dp))

        Column(modifier = Modifier.padding(horizontal = 2.dp)) {
//            Text(text = buildAnnotatedString {
//                appendInlineContent("filter")
//                append("Filter Careers")
//            }, inlineContent = mapOf("filter" to InlineTextContent(
//                Placeholder(20.sp, 20.sp, PlaceholderVerticalAlign.TextCenter)
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_baseline_filter_list_24),
//                    contentDescription = "",
//                    modifier = Modifier.fillMaxWidth(),
//                )
//            }), modifier = Modifier.padding(4.dp),
//                style = Typography.caption.copy(
//                    fontFamily = FontFamily.SansSerif
//                )
//            )

//            OutlinedTextField(
//                value = exploreViewModel.keyword.value,
//                onValueChange = { exploreViewModel.onKeywordChange(it) },
//                placeholder = { Text(text = "Type Keyword") },
//                modifier = Modifier.fillMaxWidth(),
//                textStyle = Typography.body1.copy(color = MaterialTheme.colors.onSurface)
//            )

            Row {

                FilterChip(
                    selected = exploreViewModel.filterByCat.value,
                    onClick = { exploreViewModel.filterByCatClicked() },
                    colors = ChipDefaults.filterChipColors(
                        selectedBackgroundColor = Color(0xFF0eF7729)
                    )
                ) {
                    Text(text = "category")
                }

                Spacer(modifier = Modifier.width(16.dp))

                FilterChip(
                    selected = exploreViewModel.filterByGrades.value,
                    onClick = { exploreViewModel.filterByGradesClicked() },
                    colors = ChipDefaults.filterChipColors(
                        selectedBackgroundColor = Color(0xFF0eF7729)
                    )
                ) {
                    Text(text = "career"
                    )
                }
            }
        }



        if (showLoader) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator() // Show the loader
            }
        } else {
            if (categoryList.isNotEmpty() && exploreViewModel.filterByCat.value) {

//                        CareerCategoryList(categoryList) { categoryId ->
//                            // Handle category click event here
//                            val intent = Intent(context, CategoryDetailsActivity::class.java)
//                            intent.putExtra("category_id", categoryId.toInt())
//                            context.startActivity(intent)
//                        }
//                        CareerCategoryList(categoryList) { categoryId ->
//                            // Handle category click event here
//                            exploreViewModel.selectedCategoryId = categoryId.toInt()
//                        }

                CareerCategoryList(categoryList, exploreViewModel) { categoryId ->
//                            exploreViewModel.selectedCategoryId = categoryId.toInt()
                    // Handle category click event here
                    exploreViewModel.reinitialize(context)


                    val currentSelection = exploreViewModel.selectedCategoryId
                    if (currentSelection == categoryId.toInt()) {
                        // Clicked again on the same category, toggle the selection off
                        exploreViewModel.selectedCategoryId = null
                    } else {
                        // Clicked on a different category, toggle the selection on
                        exploreViewModel.selectedCategoryId = categoryId.toInt()
                    }
                }



            }
            else if (careers.isNotEmpty() && exploreViewModel.filterByGrades.value) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(careers) { career ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp)
                                .clickable {
                                    career.id.let {
                                        Log.d("CategoryViewModel", "tione: ${career.id}")
                                        val intent = Intent(
                                            context,
                                            CareerDescriptionActivity::class.java
                                        ).apply {
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
                                career.title?.let { Text(text = it) }
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "No categories available",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }


    }
}


@Composable
fun CareerCategoryList(
    careerCategories: List<Category>,
    exploreViewModel: ExploreViewModel,
    onCategoryClicked: (String) -> Unit
) {
    val careers = exploreViewModel.careers.value
    val context = LocalContext.current // Get the current context

    Box(
        modifier = Modifier
            .background(color = Color(0xFFEEEEEE)) // Set the background color for the Box
            .fillMaxSize()
    ) {
        LazyColumn {
            items(careerCategories) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .clickable(enabled = exploreViewModel.isInitialized) {
                            onCategoryClicked(
                                category.id.toString()
                            )
                        },
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = " ${category.categoryName}",
                            style = MaterialTheme.typography.body1
                        )
                    }
                }

                val currentSelection = exploreViewModel.selectedCategoryId
                if (currentSelection != null && currentSelection == category.id) {
                    careers.forEach { career ->
                        Box(
                            modifier = Modifier
                                .clickable {
                                    career.id.let {
                                        val intent = Intent(
                                            context,
                                            CareerDescriptionActivity::class.java
                                        ).apply {
                                            putExtra("careerId", it)
                                        }
                                        context.startActivity(intent)
                                    }
                                }
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Column {
                                Text(text = career.title)
                            }
                        }
                    }
                }
            }
        }
    }

}






