package com.example.tsogolo.ui.career

import android.content.Context
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
        backArrowClicked: () -> Unit
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


                    OutlinedTextField(
                        value = careerSearchViewModel.keyword.value,
                        onValueChange = { careerSearchViewModel.onKeywordChange(it) },
                        placeholder = { Text(text = "Type Keyword") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = Typography.body1.copy(color = MaterialTheme.colors.onSurface)
                    )


                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

//                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//                    val careers = careerFinderViewModel.careers.value
//
//                    careers.forEach { careerData ->
//                        SelectableCareerItem(career = careerData) {
//                            careerFinderViewModel.careerSelectionToggled(careerData)
//                        }
//                        Divider()
//                    }
//                }

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    val careers = careerSearchViewModel.careers.value
                    Log.d("ExploreViewModel", "Category data retrieved: $careers")

                    careers.forEach { careerData ->
                        SearchItem(career = careerData, onClick = {
//                        careerFinderViewModel.careersSubmitted()
//                        context.startActivity(Intent(context, CareerGuideActivity::class.java))
                            // Navigate to the career details page
                        })
                        Divider()
                    }
                }


            }
        }
    }


class CareerSearchViewModel : ViewModel() {

    val saving: MutableState<Boolean> = mutableStateOf(false)
    val saved: MutableState<Boolean> = mutableStateOf(false)
    val filterByPersonality: MutableState<Boolean> = mutableStateOf(false)
    val filterByGrades: MutableState<Boolean> = mutableStateOf(false)
    val keyword: MutableState<String> = mutableStateOf("")
    val selectedCareers: MutableState<List<Career>> = mutableStateOf(listOf(Career()))
    val careers: MutableState<List<CareerData>> = mutableStateOf(listOf())

    private var _careers: List<Career> = listOf()
    var user = User()

    private var grades = listOf<UserSubject>()
    private var personalities = listOf<UserPersonality>()
    private var careerPrograms: Map<Career, List<ProgramAlt>> = mapOf()
    private var programRequirements: Map<Program, List<ProgramRequirement>> = mapOf()
    private var careerPersonalities: Map<Career, List<PersonalityAlt>> = mapOf()
    private var searchQuery = listOf<Career>()
    private lateinit var db: TsogoloDatabase
    private var onCareersSubmitted = {}
    var hasNoPersonality: () -> Unit = {}

    fun initialize(
        context: Context
    ) {
        this.onCareersSubmitted = onCareersSubmitted
        db = TsogoloDatabase.getInstance(context = context)

        CoroutineScope(Dispatchers.IO).launch {
//            user = db.userDao().getById(userId)
//            grades = db.userSubjectDao().allOf(userId)
//            personalities = db.userPersonalityDao().getAllOf(userId)
            careerPersonalities = db.careerDao().careerPersonalities()
            careerPrograms = db.careerDao().careerPrograms()
            programRequirements = db.programDao().programRequirements()
//            _careers = db.careerDao().searchItems(searchQuery)
            _careers = db.careerDao().getAll()
            println("Retrieved careers: $_careers")
            Log.i("CareerSearchViewModel", "Retrieved careers: $_careers")
//            selectedCareers.value = db.careerDao().getAllOf(userId)
            mapCareers()
        }
        println("Retrieved careers: $_careers")
        Log.i("CareerSearchViewModel", "Retrieved careers: $_careers")
    }

    private fun filtered(careers: List<CareerData>): List<CareerData> {
        return careers.filter {
            (!filterByGrades.value || it.gradesMatch > 0.49) &&
                    (!filterByPersonality.value || it.personalityMatch > 0.49) &&
                    (keyword.value.isBlank() ||
                            it.title.contains(keyword.value, true) ||
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
                    description = career.description,
                    personalityMatch = matchPersonality(career),
                    gradesMatch = matchGrades(career),
                    isSelected = selectedCareers.value.any { it.id == career.id }
                )
            })
        }
    }

    fun updatePersonalities() {
        CoroutineScope(Dispatchers.IO).launch {
            personalities = db.userPersonalityDao().getAllOf(user.id!!)
        }
    }

    private fun matchGrades(career: Career): Double {
        var matchedPrograms = 0
        careerPrograms[career]?.forEach { entry ->
            var matches = 0
            Log.d("LoopingP", "$matchedPrograms")

            val program = programRequirements.keys.first { it.id == entry.pr_id }
            programRequirements[program]?.forEach { pr ->
                Log.d("LoopingR", "Matches: $matches, Requirements: ${programRequirements[program]?.size}")
                grades.firstOrNull { it.subjectId == pr.subjectId }?.let {
                    if (pr.grade+1 >= it.grade ||
                        user.eduLevel != User.MSCE && pr.grade+2 >= it.grade) matches++
                }
            }

            if (matches == programRequirements[program]?.size ||
                user.eduLevel != User.MSCE && matches/ (programRequirements[program]?.size ?: 1) > 0.6) {
                matchedPrograms++
            }
        }

        val matchedRatio = matchedPrograms/if (careerPrograms[career].isNullOrEmpty()) 1.0
        else careerPrograms[career]!!.size.toDouble()
        return when {
            matchedRatio > 0.7 -> 1.0
            matchedRatio > 0.5 -> 0.75
            matchedRatio > 0.1 -> 0.5
            else -> 0.0
        }
    }

    private fun matchPersonality(career: Career): Double {
        var value = 0.0
        careerPersonalities[career]?.forEach {
            personalities.forEach { userPersonality ->
                if (it.pe_id == userPersonality.personalityId) {
                    value += 1.0/(userPersonality.rank+1)
                }
            }
        }
        return if (value > 1.0) 1.0 else value
    }

    fun deselectCareer(careerId: Int) {
        selectedCareers.value = selectedCareers.value.filter {
            it.id != careerId
        }
        mapCareers()
    }

    fun onKeywordChange(keyword: String) {
        this.keyword.value = keyword
        mapCareers()
    }

    fun filterByGradesClicked() {
        filterByGrades.value = !filterByGrades.value
        mapCareers()
    }

    fun filterByPersonalityClicked() {
        if (personalities.isEmpty()) {
            hasNoPersonality()
        } else {
            filterByPersonality.value = !filterByPersonality.value
            mapCareers()
        }
    }

    fun careerSelectionToggled(careerData: CareerData) {
        if (careerData.isSelected) {
            deselectCareer(careerData.id)
        } else {
            val careers = selectedCareers.value.toMutableList()
            careers.add(_careers.first {it.id == careerData.id})
            selectedCareers.value = careers
            mapCareers()
        }
    }

    fun careersSubmitted() {
        CoroutineScope(Dispatchers.IO).launch {
            saving.value = true
            db.preferredCareerDao().getAllOfList(user.id!!).forEach {
                db.preferredCareerDao().delete(it)
            }

            db.preferredCareerDao().insertAll(
                selectedCareers.value.mapIndexed {i, it ->
                    PreferredCareer(user.id!!, it.id!!, rank = i+1)
                }
            )
            saved.value = true
            saving.value = false

            MainScope().launch {
                onCareersSubmitted()
            }
        }
    }

}
