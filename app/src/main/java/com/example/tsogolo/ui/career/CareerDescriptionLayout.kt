package com.example.tsogolo.ui.explore

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tsogolo.database.TsogoloDatabase
import com.example.tsogolo.model.Career
import com.example.tsogolo.model.College
import com.example.tsogolo.model.Program
import com.example.tsogolo.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalUnitApi::class)
@Composable
fun careerDescription(
        careerDescriptionViewModel: CareerDescriptionViewModel = viewModel(),
        careerId: Int,
        backArrowClicked: () -> Unit
) {
        val context = LocalContext.current

        LaunchedEffect(Unit) {
                careerDescriptionViewModel.initialize(context, careerId)
        }

        val career = careerDescriptionViewModel.career.value
        val programs = careerDescriptionViewModel.program.value
        val colleges = careerDescriptionViewModel.college.value

        Column(modifier = Modifier.background(color = MaterialTheme.colors.background).fillMaxSize()) {
                IconButton(onClick = { backArrowClicked() }) {
                        Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back Arrow",
                                tint = Color.Black
                        )
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        if (career != null) {
                                CareerDetails(career = career!!,  programs = programs, colleges = colleges)
                        } else {
                                Text(text = "No career available")
                        }
                }
        }
}

class CareerDescriptionViewModel : ViewModel() {
        val career: MutableState<Career?> = mutableStateOf(null)
        val program: MutableState<List<Program>> = mutableStateOf(listOf())
        val college: MutableState<List<College>> = mutableStateOf(listOf())
//        private var careerPrograms: Map<Career, List<ProgramAlt>> = mapOf()

        private lateinit var db: TsogoloDatabase

        fun initialize(context: Context, careerId: Int) {
                db = TsogoloDatabase.getInstance(context)

                CoroutineScope(Dispatchers.IO).launch {
                        try {
                                val careersById = db.careerDao().getCareerById(careerId)
                                val careerPrograms = db.programDao().getProgramsForCareer(careerId)
                                val ProgramCollege = db.collegeDao().getAllCollege(careerId)
                                career.value = careersById.firstOrNull()
                                program.value = careerPrograms
                                college.value = ProgramCollege
                                Log.d("CategoryViewModel", "Careers program id: $careerPrograms")
                                Log.d("CategoryViewModel", "Careers retrieved: $careersById")
                        } catch (e: Exception) {
                                Log.e("CategoryViewModel", "Failed to retrieve careers: ${e.message}")
                        }
                }
        }
}

@Composable
fun CareerDetails(career: Career, programs: List<Program>, colleges: List<College>) {
        Column(
                modifier = Modifier.padding(16.dp)
        ) {
                Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                ) {
                        Column(
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                                Box(
                                        modifier = Modifier
                                                .size(80.dp)
                                                .background(color = MaterialTheme.colors.primary),
                                        contentAlignment = Alignment.Center
                                ) {
                                        career.title?.substring(0, 2)?.let {
                                                Text(
                                                        text = it.uppercase(Locale.ENGLISH),
                                                        style = Typography.h3.copy(color = Color.White),
                                                        textAlign = TextAlign.Center
                                                )
                                        }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                        text = career.title.toString(),
                                        style = Typography.h6.copy(fontWeight = FontWeight.Bold),
                                        textAlign = TextAlign.Center
                                )
                        }
                }






                Spacer(modifier = Modifier.height(8.dp))


                Text(
                        text = "Overview",
                        style = Typography.h6.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                        text = career.description.toString(),
                        style = Typography.body1,
                        textAlign = TextAlign.Justify
                )

                if (programs.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                                text = "Available Programs:",
                                style = Typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                                textAlign = TextAlign.Start
                        )

                        programs.forEach { program ->
                                Text(
                                        text = program.name.toString(),
                                        style = Typography.body1,
                                        textAlign = TextAlign.Start
                                )
                        }
                }

                if (colleges.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                                text = "Available Colleges:",
                                style = Typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                                textAlign = TextAlign.Start
                        )

                        colleges.forEach { college ->
                                Text(
                                        text = college.name.toString(),
                                        style = Typography.body1,
                                        textAlign = TextAlign.Start
                                )
                        }
                }
        }
}

@Composable
fun ProfileEntry(key: String, value: String) {
        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                        text = key,
                        style = Typography.subtitle2.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif
                        ),
                        modifier = Modifier.width(128.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                        text = value,
                        style = Typography.subtitle2.copy(
                                fontWeight = FontWeight.Normal,
                                fontFamily = FontFamily.SansSerif
                        )
                )
        }
}
