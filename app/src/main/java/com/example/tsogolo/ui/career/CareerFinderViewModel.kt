package com.example.tsogolo.ui.career

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tsogolo.database.PersonalityAlt
import com.example.tsogolo.database.ProgramAlt
import com.example.tsogolo.database.TsogoloDatabase
import com.example.tsogolo.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class CareerFinderViewModel : ViewModel() {

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

    private lateinit var db: TsogoloDatabase
    private var onCareersSubmitted = {}
    var hasNoPersonality: () -> Unit = {}

    fun initialize(
        context: Context,
        userId: Int,
        onCareersSubmitted: () -> Unit
    ) {
        this.onCareersSubmitted = onCareersSubmitted
        db = TsogoloDatabase.getInstance(context = context)

        CoroutineScope(IO).launch {
            user = db.userDao().getById(userId)
            grades = db.userSubjectDao().allOf(userId)
            personalities = db.userPersonalityDao().getAllOf(userId)
            careerPersonalities = db.careerDao().careerPersonalities()
            careerPrograms = db.careerDao().careerPrograms()
            programRequirements = db.programDao().programRequirements()

            _careers = db.careerDao().getAll()
            selectedCareers.value = db.careerDao().getAllOf(userId)
            mapCareers()
        }
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
        CoroutineScope(Default).launch {
            this@CareerFinderViewModel.careers.value = filtered(_careers.map { career ->
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
        CoroutineScope(IO).launch {
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
        CoroutineScope(IO).launch {
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

data class CareerData(
    val id: Int,
    val title: String,
    val aas: Double,
    val personalityMatch: Double = 0.0,
    val gradesMatch: Double = 0.8,
    val description: String?,
    val isSelected: Boolean = false
)
