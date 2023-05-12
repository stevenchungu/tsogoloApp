package com.example.tsogolo.ui.profile

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tsogolo.database.TsogoloDatabase
import com.example.tsogolo.model.Subject
import com.example.tsogolo.model.User
import com.example.tsogolo.model.UserSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class ProfilingViewModel : ViewModel() {
    val years = 1950 until GregorianCalendar().get(GregorianCalendar.YEAR)
    val months = 1..12
    val days = 1..31
    val eduLevels = arrayOf(User.MSCE, User.JCE, User.PLSCE)

    var profileData: MutableState<ProfileData> = mutableStateOf(
        ProfileData(
            year = mutableStateOf(1950),
            month = mutableStateOf(1),
            day = mutableStateOf(1)
        )
    )
    private set

    private lateinit var db: TsogoloDatabase

    private var onSaved: () -> Unit = {}

    private var user: User? = null

    suspend fun initialize(context: Context, uid: Int?, onSaved: () -> Unit) {
        db = TsogoloDatabase.getInstance(context)
        this.onSaved = onSaved

        db.subjectDao().getAll().collectLatest { subjects ->
            val profileData = this.profileData.value
            profileData.subjectGrades.value = subjects.map {
                Pair(it, "")
            }

            this.profileData.setValue(this, this::profileData, profileData)
            Log.d("PROFILING", "UserId1: $uid")

            if (uid != null) {
                user = db.userDao().getById(uid)
                nameChanged(user!!.name!!)
                onEdulevelSelected(user!!.eduLevel)
                db.userSubjectDao().allOf(uid).forEach { userSubject ->
                    onGradeChange(
                        this.profileData.value.subjectGrades.value.indexOfFirst { it.first.id == userSubject.subjectId },
                        userSubject.grade.toString()
                    )
                }
                GregorianCalendar().apply {
                    time = user!!.dob!!
                    this@ProfilingViewModel.profileData.value.day.value = get(GregorianCalendar.DAY_OF_MONTH)
                    this@ProfilingViewModel.profileData.value.month.value = get(GregorianCalendar.MONTH)+1
                    this@ProfilingViewModel.profileData.value.year.value = get(GregorianCalendar.YEAR)
                }
                Log.d("PROFILING", "User: $user")
            }
        }

        Log.d("PROFILING", "UserId2: $uid")
    }

    fun nameChanged(name: String) {
        profileData.value.name.value = name
    }

    fun onEdulevelSelected(level: String) {
        profileData.value.eduLevel.value = level
    }

    fun onGradeChange(index: Int, grade: String) {
        if (grade.length < 2 && grade != "0") {
            val grades = profileData.value.subjectGrades.value.toMutableList()
            grades[index] = grades[index].first to grade
            profileData.value.subjectGrades.value = grades
        }

    }

    fun saveProfile() {
        CoroutineScope(IO).launch {
            profileData.value.saving.setValue(this, profileData.value::saving, true)

            val newUser = User.Builder()
                .setName(profileData.value.name.value)
                .setDob(GregorianCalendar(
                    profileData.value.year.value,
                    profileData.value.month.value-1,
                    profileData.value.day.value,
                ).time)
                .setEduLevel(profileData.value.eduLevel.value)
                .build().also { it.id = user?.id }
            var uid = user?.id?.toLong()
            if (user != null) {
                db.userDao().update(newUser)
                db.userSubjectDao().allOf(userId = user!!.id!!).forEach {
                    db.userSubjectDao().delete(it)
                }
            } else {
                uid = db.userDao().insert(newUser)
            }

            profileData.value.subjectGrades.value.forEach {
                val grade = it.second.toIntOrNull()
                if (grade != null) {
                    val userSubject = UserSubject(
                        userId = uid!!.toInt(),
                        subjectId = it.first.id!!,
                        grade = grade
                    )

                    db.userSubjectDao().insertAll(listOf(userSubject))
                }
            }

            profileData.value.saving.setValue(this, profileData.value::saving, false)
            profileData.value.saved.setValue(this, profileData.value::saved, true)
            onSaved()
        }
    }
}

data class ProfileData(
    var subjectGrades: MutableState<List<Pair<Subject, String>>> = mutableStateOf(listOf()),
    var year: MutableState<Int>,
    var month: MutableState<Int>,
    var day: MutableState<Int>,
    var name: MutableState<String> = mutableStateOf(""),
    var eduLevel: MutableState<String> = mutableStateOf(User.MSCE),
    var saving: MutableState<Boolean> = mutableStateOf(false),
    var saved: MutableState<Boolean> = mutableStateOf(false),
)