package com.example.tsogolo.ui.career

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tsogolo.R
import com.example.tsogolo.ui.components.SelectableCareerItem
import com.example.tsogolo.ui.theme.Typography

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CareerFinderLayout(
    careerFinderViewModel: CareerFinderViewModel = viewModel(),
    backArrowClicked: () -> Unit
) {

    Column(modifier = Modifier.background(color = MaterialTheme.colors.background)
        .fillMaxSize()) {
        TopAppBar(
            title = { Text("Identify Suitable Career") },
            navigationIcon = { Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back Arrow",
                Modifier.clickable { backArrowClicked() },
            )},
            backgroundColor = MaterialTheme.colors.background,

        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Box(modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp, bottom = 8.dp)) {
                Text(
                    text = "Selected Careers",
                    style = Typography.caption.copy(
                        fontFamily = FontFamily.SansSerif
                    )
                )
            }
            LazyRow(modifier = Modifier.padding(horizontal = 16.dp)) {
                val selectedCareers = careerFinderViewModel.selectedCareers.value

                items(selectedCareers) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(Color(100, 128, 228, 100), shape = RoundedCornerShape(8.dp))
                    ) {
                        Text(text = buildAnnotatedString {
                            appendInlineContent("deselect")
                            append(it.title ?: "Unknown")
                        }, inlineContent = mapOf("deselect" to InlineTextContent(
                            Placeholder(20.sp, 20.sp, PlaceholderVerticalAlign.TextCenter)
                        ) { _ ->
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "",
                                tint = Color.Red,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        careerFinderViewModel.deselectCareer(it.id ?: -1)
                                    }
                            )
                        }), modifier = Modifier.padding(4.dp),
                            style = Typography.body2.copy(
                                fontWeight = FontWeight.Normal,
                                fontFamily = FontFamily.SansSerif,
                                color = MaterialTheme.colors.onSurface
                            )
                        )
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = buildAnnotatedString {
                    appendInlineContent("filter")
                    append("Filter Careers")
                }, inlineContent = mapOf("filter" to InlineTextContent(
                    Placeholder(20.sp, 20.sp, PlaceholderVerticalAlign.TextCenter)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_filter_list_24),
                        contentDescription = "",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }), modifier = Modifier.padding(4.dp),
                    style = Typography.caption.copy(
                        fontFamily = FontFamily.SansSerif
                    )
                )

                OutlinedTextField(
                    value = careerFinderViewModel.keyword.value,
                    onValueChange = { careerFinderViewModel.onKeywordChange(it) },
                    placeholder = { Text(text = "Type Keyword") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = Typography.body1.copy(color = MaterialTheme.colors.onSurface)
                )

                Row {
                    FilterChip(
                        selected = careerFinderViewModel.filterByGrades.value,
                        onClick = { careerFinderViewModel.filterByGradesClicked() },
                        colors = ChipDefaults.filterChipColors(
                            selectedBackgroundColor = Color(0xFF0eF7729)
                        )
                    ) {
                        Text(text = "Grades Match")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    FilterChip(
                        selected = careerFinderViewModel.filterByPersonality.value,
                        onClick = { careerFinderViewModel.filterByPersonalityClicked() },
                        colors = ChipDefaults.filterChipColors(
                            selectedBackgroundColor = Color(0xFF0eF7729)
                        )
                    ) {
                        Text(text = "Personality Match"
                            )
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                val careers = careerFinderViewModel.careers.value
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "Choose Careers",
                    style = Typography.caption.copy(

                        fontFamily = FontFamily.SansSerif
                    )
                )
                careers.forEach { careerData ->
                    SelectableCareerItem(career = careerData) {
                        careerFinderViewModel.careerSelectionToggled(careerData)
                    }
                    Divider()
                }
            }

            Divider(modifier = Modifier.padding(vertical = 4.dp))


            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
                    .height(56.dp),
                enabled = careerFinderViewModel.selectedCareers.value.isNotEmpty()
                            && !careerFinderViewModel.saving.value && !careerFinderViewModel.saved.value,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF0eF7729)),
                onClick = {
                    careerFinderViewModel.careersSubmitted()
                }
            ) {
                Text(text = when {
                    careerFinderViewModel.saving.value -> "Submitting..."
                    careerFinderViewModel.saved.value -> "Submitted"
                    else -> "Submit Careers"
                },
                    color = Color.White
                )
            }
        }
    }
}