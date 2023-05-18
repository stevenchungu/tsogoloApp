package com.example.tsogolo.ui.explore

//import com.example.tsogolo.ui.MenuCard
import android.app.Application
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tsogolo.model.Category
import com.example.tsogolo.model.User
import com.example.tsogolo.ui.explore.MenuItem
import com.example.tsogolo.ui.career.CareerFinderActivity
import com.example.tsogolo.ui.components.*
//import com.example.tsogolo.ui.grades.GradesActivity
import com.example.tsogolo.ui.personality.PersonalityTestActivity
import com.example.tsogolo.ui.theme.Typography
import kotlinx.coroutines.flow.firstOrNull



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

    val categoryList = exploreViewModel.categor.value

    Column(modifier = Modifier.fillMaxSize()) {

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
                    context.startActivity(Intent(context, CareerFinderActivity::class.java))
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Black
                    )
                }
            }
        )

        Column(modifier = Modifier.shadow(2.dp)) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "Subjects To Focus On",
                    style = Typography.subtitle2.copy(
                        color = MaterialTheme.colors.primary,
                        fontFamily = FontFamily.SansSerif
                    )
                )

                if (categoryList.isNotEmpty()) {
                    CareerCategoryList(categoryList) { categoryId ->
                        // Handle category click event here, e.g., store the category ID
                        // or perform any other actions
                    }
                } else {
                    Text(text = "No categories available")
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
                    .padding(8.dp)
                    .clickable { onCategoryClicked(category.id.toString()) },
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Category ID: ${category.id}",
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = "Category Name: ${category.categoryName}",
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}





