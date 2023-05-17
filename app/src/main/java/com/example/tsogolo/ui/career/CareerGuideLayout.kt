package com.example.tsogolo.ui.career

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tsogolo.model.User
import com.example.tsogolo.ui.components.CollegeItem
import com.example.tsogolo.ui.components.ProgramItem
import com.example.tsogolo.ui.components.SubjectItem
import com.example.tsogolo.ui.theme.Typography


@Composable
fun CareerGuideLayout(
    careerGuideViewModel: CareerGuideViewModel = viewModel(),
    backArrowClicked: () -> Unit
) {
Box(modifier = Modifier
    .fillMaxSize()
    .background(color = MaterialTheme.colors.background)

){
    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .background(color = MaterialTheme.colors.background)
        .fillMaxSize()) {
        TopAppBar(title = { Text("Career Guide") }, navigationIcon = { Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back Arrow",
            Modifier.clickable { backArrowClicked() },

        )
        },
            backgroundColor = MaterialTheme.colors.background)

        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = "Preferred Careers",
                style = Typography.caption.copy(
                    fontFamily = FontFamily.SansSerif
                )
            )
        }
        LazyRow(modifier = Modifier.padding(horizontal = 16.dp)) {
            val selectedCareers = careerGuideViewModel.selectedCareers.value

            items(selectedCareers) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color(100, 128, 228, 100), shape = RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = it.title ?: "Unknown",
                        modifier = Modifier.padding(4.dp),
                        style = Typography.body2.copy(
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.SansSerif,
                            color = MaterialTheme.colors.onSurface
                        )
                    )
                }
            }
        }

        Column(modifier = Modifier.shadow(2.dp)) {
            if (careerGuideViewModel.user.eduLevel != User.MSCE) {
                val subjects = careerGuideViewModel.subjects.value
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = "Subjects To Focus On",
                        style = Typography.subtitle2.copy(
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                    subjects.forEachIndexed {i, it ->
                        SubjectItem(subject = it)
                        if (i != subjects.lastIndex) {
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }
            }

            val programs = careerGuideViewModel.programs.value
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "Programs To Study",
                    style = Typography.subtitle2.copy(
                        fontFamily = FontFamily.SansSerif
                    )
                )
                programs.forEachIndexed {i, it ->
                    var isExpanded by remember { mutableStateOf(false) }
                    Divider()
                    ProgramItem(program = it)
                    Text(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .clickable { isExpanded = !isExpanded },
                        text = "Colleges",
                        style = Typography.caption.copy(
                            color = Color(0xFF0eF7729),
                            fontFamily = FontFamily.SansSerif
                        ),
                        textDecoration = TextDecoration.Underline
                    )
                    if (isExpanded) {
                        Column(
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            careerGuideViewModel.colleges.value[it]?.forEach {
                                CollegeItem(college = it)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Button(onClick = { backArrowClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF0eF7729))
        ) {
            Text(text = "DONE",
            color = Color.White
                )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
}