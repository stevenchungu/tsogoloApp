package com.example.tsogolo.ui.career;

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable;
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDirection.Companion.Content
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.TableInfo
import com.example.tsogolo.ui.components.SearchItem
import com.example.tsogolo.ui.theme.Typography

import kotlin.OptIn;

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CareerDescriptionLayout(
        careerDescriptionViewModel: CareerDescriptionViewModel = viewModel(),
        backArrowClicked: () -> Unit
        ) {

        val context = LocalContext.current

//        LaunchedEffect(Unit) {
//                careerSearchViewModel.initialize(context)
//        }

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

//                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
//
//                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//
//
//                                OutlinedTextField(
//                                        value = careerSearchViewModel.keyword.value,
//                                        onValueChange = { careerSearchViewModel.onKeywordChange(it) },
//                                        placeholder = { Text(text = "Type Keyword") },
//                                        modifier = Modifier.fillMaxWidth(),
//                                        textStyle = Typography.body1.copy(color = MaterialTheme.colors.onSurface)
//                                )
//
//
//                        }
//
//                        Divider(modifier = Modifier.padding(vertical = 8.dp))
//
////                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
////                    val careers = careerFinderViewModel.careers.value
////
////                    careers.forEach { careerData ->
////                        SelectableCareerItem(career = careerData) {
////                            careerFinderViewModel.careerSelectionToggled(careerData)
////                        }
////                        Divider()
////                    }
////                }
//
//                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//                                val careers = careerSearchViewModel.careers.value
//                                Log.d("ExploreViewModel", "Category data retrieved: $careers")
//
//                                careers.forEach { careerData ->
//                                        SearchItem(career = careerData, onClick = {
////                        careerFinderViewModel.careersSubmitted()
////                        context.startActivity(Intent(context, CareerGuideActivity::class.java))
//                                                // Navigate to the career details page
//                                        })
//                                        Divider()
//                                }
//                        }
//
//
//                }
        }
}

class CareerDescriptionViewModel : ViewModel() {

}

//@Composable
//fun ParallaxToolbar(recipe: Recipe, scrollState: LazyListState) {
//        val imageHeight = AppBarExpendedHeight - AppBarCollapsedHeight
//
//        val maxOffset =
//                with(LocalDensity.current) { imageHeight.roundToPx() } - LocalWindowInsets.current.systemBars.layoutInsets.top
//
//        val offset = min(scrollState.firstVisibleItemScrollOffset, maxOffset)
//
//        val offsetProgress = max(0f, offset * 3f - 2f * maxOffset) / maxOffset
//
//        TopAppBar(
//                contentPadding = PaddingValues(),
//                backgroundColor = White,
//                modifier = Modifier
//                        .height(
//                                AppBarExpendedHeight
//                        )
//                        .offset { IntOffset(x = 0, y = -offset) },
//                elevation = if (offset == maxOffset) 4.dp else 0.dp
//        ) {
//                Column {
//                        Box(
//                                Modifier
//                                        .height(imageHeight)
//                                        .graphicsLayer {
//                                                alpha = 1f - offsetProgress
//                                        }) {
//                                Image(
//                                        painter = painterResource(id = R.drawable.strawberry_pie_1),
//                                        contentDescription = null,
//                                        contentScale = ContentScale.Crop,
//                                        modifier = Modifier.fillMaxSize()
//                                )
//
//                                Box(
//                                        modifier = Modifier
//                                                .fillMaxSize()
//                                                .background(
//                                                        Brush.verticalGradient(
//                                                                colorStops = arrayOf(
//                                                                        Pair(0.4f, Transparent),
//                                                                        Pair(1f, White)
//                                                                )
//                                                        )
//                                                )
//                                )
//
//                                Row(
//                                        modifier = Modifier
//                                                .fillMaxHeight()
//                                                .padding(horizontal = 16.dp, vertical = 8.dp),
//                                        verticalAlignment = Alignment.Bottom
//                                ) {
//                                        Text(
//                                                recipe.category,
//                                                fontWeight = FontWeight.Medium,
//                                                modifier = Modifier
//                                                        .clip(Shapes.small)
//                                                        .background(LightGray)
//                                                        .padding(vertical = 6.dp, horizontal = 16.dp)
//                                        )
//                                }
//                        }
//                        Column(
//                                Modifier
//                                        .fillMaxWidth()
//                                        .height(AppBarCollapsedHeight),
//                                verticalArrangement = Arrangement.Center
//                        ) {
//                                Text(
//                                        recipe.title,
//                                        fontSize = 26.sp,
//                                        fontWeight = FontWeight.Bold,
//                                        modifier = Modifier
//                                                .padding(horizontal = (16 + 28 * offsetProgress).dp)
//                                                .scale(1f - 0.25f * offsetProgress)
//                                )
//
//                        }
//                }
//        }
//
//        Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier
//                        .fillMaxWidth()
//                        .statusBarsPadding()
//                        .height(AppBarCollapsedHeight)
//                        .padding(horizontal = 16.dp)
//        ) {
//                CircularButton(R.drawable.ic_arrow_back)
//                CircularButton(R.drawable.ic_favorite)
//        }
//}


