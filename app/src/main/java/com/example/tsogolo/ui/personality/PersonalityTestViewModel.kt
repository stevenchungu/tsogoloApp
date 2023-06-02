package com.example.tsogolo.ui.personality

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tsogolo.database.TsogoloDatabase
import com.example.tsogolo.model.Personality
import com.example.tsogolo.model.PersonalityQuestion
import com.example.tsogolo.model.User
import com.example.tsogolo.model.UserPersonality
import com.example.tsogolo.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class PersonalityTestViewModel() : ViewModel() {

    val canSubmit: MutableState<Boolean> = mutableStateOf(false)
    val question: MutableState<QuestionData> = mutableStateOf(QuestionData(0,))


    private var questions: List<QuestionData> = listOf()
    private var user: User = User()
    private lateinit var db: TsogoloDatabase
    private var index = 0
    private var personalities = listOf<Personality>()
    var onSubmitted: () -> Unit = {}

    //progress bar variables
    var progressValue: MutableState<Float> = mutableStateOf(0f)
    private var answeredQuestionCount = 0
    private val totalQuestions: Int = 20
    var apiResponse : List<PersonalityQuestion> by mutableStateOf(listOf())
    var errorMassage : String by mutableStateOf("")
    fun initialize(context: Context, userId: Int, onSubmitted: () -> Unit) {

        this.onSubmitted = onSubmitted
        db = TsogoloDatabase.getInstance(context)
        CoroutineScope(IO).launch {

            val apiService = ApiService.getInstance()
            try {
                val questionData = apiService.getPersonalityQuestions()
                apiResponse = questionData
                Log.d("Questions", apiResponse.toString())
            }
            catch (e:Exception){
                errorMassage = e.message.toString()
                Log.d("Questions", "error occured $errorMassage")
            }

            for (question in apiResponse) {
                val questionId = question.id
                val questionText = question.question
                // Access other properties as needed
                Log.d("Question ID", questionId.toString())
                if (questionText != null) {
                    Log.d("Question Text", questionText)
                }
            }


            user = db.userDao().getById(userId)
            questions = db.personalityQuestionDao().getAll().mapIndexed {i, it ->
                QuestionData(
                    id = it.id!!,
                    question = "${i+1}.  " + it.question!!,
                    agreedType = it.agreeType.toCharArray()[0],
                    denyType = it.denialType.toCharArray()[0]
                )
            }
            questions.firstOrNull()?.let {
                question.value = it
            }

            personalities = db.personalityDao().getAll()
        }
    }

    fun questionsSubmitted() {
        // Progress calculations

        val progress = answeredQuestionCount.toFloat() / totalQuestions.toFloat()
        progressValue.value = progress

        val (I, cI) = countChar('I')
        val (N, cN) = countChar('N')
        val (T, cT) = countChar('T')
        val (J, cJ) = countChar('J')

        val rI = I/cI
        val rN = N/cN
        val rT = T/cT
        val rJ = J/cJ
        val isLeastLikely = {r: Double, r1: Double, r2: Double, r3: Double ->
            val likelihood = {d: Double -> if (d < 0.5) 1-d else d}
            (likelihood(r) <= likelihood(r1) && likelihood(r) <= likelihood(r2) && likelihood(r) <= likelihood(r3))
        }

        val type = (if (rI >= 0.5) "I" else "E") +
                (if (rN >= 0.5) "N" else "S") +
                (if (rT >= 0.5) "T" else "F") +
                (if (rJ >= 0.5) "J" else "P")

        val type2 = String(type.toCharArray().mapIndexed { index, c ->
            when (index) {
                0 -> if (isLeastLikely(rI, rN, rJ, rT)) (if (c == 'I') 'E' else 'I') else c
                1 -> if (isLeastLikely(rN, rI, rJ, rT)) (if (c == 'N') 'S' else 'N') else c
                2 -> if (isLeastLikely(rT, rN, rJ, rI)) (if (c == 'T') 'F' else 'T') else c
                3 -> if (isLeastLikely(rJ, rN, rI, rT)) (if (c == 'J') 'P' else 'J') else c
                else -> c
            }
        }.toCharArray())

        CoroutineScope(IO).launch {
            db.userPersonalityDao().getAllOf(user.id!!).forEach {
                db.userPersonalityDao().delete(it)
            }
            val p1 = personalities.first { it.type == type }
            val p2 = personalities.first { it.type == type2 }
            db.userPersonalityDao()
                .insertAll(listOf(
                    UserPersonality(
                        user.id!!,
                        p1.id!!,
                        rank = 0
                    ),
                    UserPersonality(
                        user.id!!,
                        p2.id!!,
                        rank = 1
                    )
                )
                )
            onSubmitted()
        }
    }

    private fun countChar(char: Char): Pair<Int, Double> {
        var c = 0.0
        var cA = 0
        questions.forEach {
            if (it.agreedType == char || it.denyType == char) {
                c++
                if (it.agreedType == char && it.agreed.value || it.denyType == char && it.denied.value) {
                    cA++
                }
            }

            Log.e("SUBS", "char: $char, Type: ${it.agreedType} == ${it.agreedType == char}")
        }

        return cA to if (c == 0.0) 1.0 else c
    }

    fun nextQuestion() {
        if (canGoForward()) {
            index++
            setQuestion()
        }
    }

    fun previousQuestion() {
        if (canGoBack()) {
            index--
            setQuestion()
        }
    }

    fun canGoBack(): Boolean {
        return index > 0
    }


    fun canGoForward(): Boolean {
        return index < questions.size - 1
    }


    fun questionResponded(response: Boolean) {
        // Only increment answered question count if a radio button is clicked
        if (!question.value.agreed.value && !question.value.denied.value) {
            // Increment answered question count
            answeredQuestionCount++

            // Progress calculations
            val progress = answeredQuestionCount.toFloat() / totalQuestions.toFloat()
            progressValue.value = progress
        }


        if (response) {
            question.value.agreed.value = true
            question.value.denied.value = false
        } else {
            question.value.agreed.value = false
            question.value.denied.value = true
        }
        setQuestion()
        canSubmit.value = questions.all {
            it.agreed.value || it.denied.value
        }
    }

    private fun setQuestion() {
        if (questions.isNotEmpty()) {
            question.value = questions[index]
        }
    }
}

data class QuestionData(
    val id: Int = 0,
    val question: String = "Question",
    val agreedType: Char = 'I',
    val denyType: Char = 'E',
    var agreed: MutableState<Boolean> = mutableStateOf(false),
    var denied: MutableState<Boolean> = mutableStateOf(false),
)

