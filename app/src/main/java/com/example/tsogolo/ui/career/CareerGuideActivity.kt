package com.example.tsogolo.ui.career

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tsogolo.database.TsogoloDatabase
import com.example.tsogolo.model.*
import com.example.tsogolo.ui.theme.TsogoloTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class CareerGuideActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[CareerGuideViewModel::class.java]

        viewModel.initialize(this.applicationContext, intent.getIntExtra(EXTRA_USER_ID, -1))

        setContent {
            TsogoloTheme(this.applicationContext) {
                CareerGuideLayout(viewModel) {
                    finish()
                }
            }
        }
    }

    companion object {
        private const val EXTRA_USER_ID = "user_id"

        @JvmStatic
        fun start(context: Context, user: User) {
            val starter = Intent(context, CareerGuideActivity::class.java)
                .putExtra(EXTRA_USER_ID, user.id!!)
            context.startActivity(starter)
        }
    }
}

class CareerGuideViewModel : ViewModel() {

    val selectedCareers: MutableState<List<Career>> = mutableStateOf(listOf(Career()))
    var user = User()

    val subjects: MutableState<List<Subject>> = mutableStateOf(listOf())
    val colleges: MutableState<Map<Program, List<College>>> = mutableStateOf(mapOf())
    val programs: MutableState<List<Program>> = mutableStateOf(listOf(Program()))

    fun initialize(context: Context, userId: Int) {
        val db = TsogoloDatabase.getInstance(context)

        CoroutineScope(IO).launch {
            user = db.userDao().getById(userId)
            selectedCareers.value = db.careerDao().getAllOf(userId)
            programs.value = db.programDao().getAllOf(userId)

            val collegesMap = mutableMapOf<Program, List<College>>()
            programs.value.forEach {
                collegesMap[it] = db.collegeDao().getAllProgram(it.id)
            }
            colleges.value = collegesMap.toMap()
            subjects.value = db.subjectDao().getAllOfFromCareers(userId)
        }
    }
}