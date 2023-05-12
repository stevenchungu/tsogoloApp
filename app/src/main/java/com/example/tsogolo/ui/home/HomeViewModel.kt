package com.example.tsogolo.ui.home

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tsogolo.database.TsogoloDatabase
import com.example.tsogolo.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    val subjects: MutableState<List<Subject>> = mutableStateOf(listOf())
    val programs: MutableState<List<Program>> = mutableStateOf(listOf(Program()))
    val careers: MutableState<List<Career>> = mutableStateOf(listOf(Career()))
    val activePersonalities: MutableState<List<Personality>> = mutableStateOf(listOf(Personality()))
    val activeUser: MutableState<User> = mutableStateOf(User())
    val users: MutableState<List<User>> = mutableStateOf(listOf(User()))
    val colleges: MutableState<Map<Program, List<College>>> = mutableStateOf(mapOf())

    private var db: TsogoloDatabase? = null
    private var activeUserId: Int? = null

    fun initialize(context: Context) {
        db = TsogoloDatabase.getInstance(context)
        setupData(db!!)
    }

    private fun setupData(db: TsogoloDatabase) {
        CoroutineScope(IO).launch {
            db.userDao().getAll().collectLatest { users ->
                this@HomeViewModel.users.value = users
                if (users.isNotEmpty()) {
                    if (activeUserId == null) {
                        activeUser.value = users[0]
                        activeUserId = activeUser.value.id
                    } else {
                        activeUser.value = users.first { it.id == activeUserId }
                    }
                    activePersonalities.value = db.personalityDao().getAllOf(activeUser.value.id!!)

                    this@HomeViewModel.careers.value = db.careerDao().getAllOf(activeUser.value.id!!)

                    if (activeUser.value.eduLevel != User.MSCE) {
                        subjects.value = db.subjectDao().getAllOfFromCareers(activeUser.value.id!!)
                    }
                    programs.value = db.programDao().getAllOf(activeUser.value.id!!)

                    val collegesMap = mutableMapOf<Program, List<College>>()
                    programs.value.forEach {
                        collegesMap[it] = db.collegeDao().getAllProgram(it.id)
                    }
                    colleges.value = collegesMap.toMap()
                }
            }
        }
    }

    fun userSwitched(user: User) {
        activeUserId = user.id
        setupData(db!!)
    }

    fun resumed() {
        db?.let { setupData(it) }
    }

    fun deleteUser(user: User) {
        CoroutineScope(IO).launch {
            db?.userDao()?.delete(user)
            if (activeUserId == user.id) activeUserId = null
            db?.let { setupData(it) }
        }
    }
}