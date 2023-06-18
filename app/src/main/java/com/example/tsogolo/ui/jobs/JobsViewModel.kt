package com.example.tsogolo.ui.jobs

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tsogolo.database.TsogoloDatabase
import com.example.tsogolo.model.Personality
import com.example.tsogolo.model.User
import com.example.tsogolo.models.Job
import com.example.tsogolo.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.collectLatest

class JobsViewModel : ViewModel() {
    private val jobRepository = JobRepository() // Assuming you have a JobRepository implementation
     val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean>
        get() = _isLoading

    lateinit var db: TsogoloDatabase
    val activePersonalities: MutableState<List<Personality>> = mutableStateOf(listOf(Personality()))
    val activeUser: MutableState<User> = mutableStateOf(User())
    val users: MutableState<List<User>> = mutableStateOf(listOf(User()))
    private var activeUserId: Int? = null


    private val _jobs: MutableState<List<Job>> = mutableStateOf(listOf())
    val jobs: State<List<Job>>
        get() = _jobs

    private var allJobs = listOf<Job>()

    private val _selectedCategories: MutableState<List<String>> = mutableStateOf(listOf())
    val selectedCategories: State<List<String>>
        get() = _selectedCategories


    private val _searchQuery: MutableState<String> = mutableStateOf("")

    val searchQuery: State<String>
        get() = _searchQuery




    fun loadJobs(context: Context) {
        _isLoading.value = true
         db = TsogoloDatabase.getInstance(context)

        // Retrieve jobs from the repository or API using coroutines
        CoroutineScope(Dispatchers.IO).launch {

            try {
                db.userDao().getAll().collectLatest { users ->
                    this@JobsViewModel.users.value = users
                    if (users.isNotEmpty()) {
                        if (activeUserId == null) {
                            activeUser.value = users[0]
                            activeUserId = activeUser.value.id
                        } else {
                            activeUser.value = users.first { it.id == activeUserId }
                        }
                        activePersonalities.value = db.personalityDao().getAllOf(activeUser.value.id!!)

                        if (activePersonalities.value.isNotEmpty()) {
                            val fetchedJobs = jobRepository.getJobs(activePersonalities.value[0].type!!) // Pass the personalityType parameter here
                            allJobs = fetchedJobs
                            _jobs.value = fetchedJobs
                            Log.d("Joblessss", activePersonalities.value[0].type!!)

                        } else {
                            val fetchedJobs = jobRepository.getJobs("INTJ") // Pass the personalityType parameter here
                            allJobs = fetchedJobs
                            _jobs.value = fetchedJobs
                        }
                    }
                }
                Log.d("Typeee", activePersonalities.value[0].type.toString())



            } catch (e: Exception) {
                // Handle any errors or exceptions here
                // For example, you can show an error message to the user
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        updateJobs()
    }

    fun toggleCategorySelection(category: String) {
        val selectedCategories = _selectedCategories.value.toMutableList()
        if (selectedCategories.contains(category)) {
            selectedCategories.remove(category)
        } else {
            selectedCategories.add(category)
        }
        _selectedCategories.value = selectedCategories
        updateJobs()
    }

    fun updateJobs() {
        val jobs = if (_selectedCategories.value.isEmpty()) allJobs else allJobs.filter { job ->
            _selectedCategories.value.contains(job.sector)
        }
        _jobs.value = jobs.filter {
            it.title.contains(searchQuery.value, ignoreCase = true) ||
            it.sector.contains(searchQuery.value, ignoreCase = true) ||
            it.location.contains(searchQuery.value, ignoreCase = true)
        }
    }

    fun getJob(jobId: Int): Job {
        return allJobs.first { it.id == jobId }
    }
}

class JobRepository {

    suspend fun getJobs(personalityType: String): List<Job> {
        return ApiService.getInstance().getJobs(personalityType) // Pass the personalityType parameter here
    }
}
