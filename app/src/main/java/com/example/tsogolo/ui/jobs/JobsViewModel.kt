package com.example.tsogolo.ui.jobs

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tsogolo.models.Job
import com.example.tsogolo.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JobsViewModel : ViewModel() {
    private val jobRepository = JobRepository() // Assuming you have a JobRepository implementation

    private val _jobs: MutableState<List<Job>> = mutableStateOf(listOf())
    val jobs: State<List<Job>>
        get() = _jobs

    private var allJobs = listOf<Job>()

    private val _selectedCategories: MutableState<List<String>> = mutableStateOf(listOf())
    val selectedCategories: State<List<String>>
        get() = _selectedCategories

    val jobCategories: List<String> = listOf("Engineering", "Design", "Sales", "Marketing", "Customer Support")

    private val _searchQuery: MutableState<String> = mutableStateOf("")
    val searchQuery: State<String>
        get() = _searchQuery

    init {
        // Perform initial data loading or setup here
        loadJobs()
    }

    fun loadJobs() {
        // Retrieve jobs from the repository or API using coroutines
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fetchedJobs = jobRepository.getJobs()
                allJobs = fetchedJobs
                _jobs.value = fetchedJobs
            } catch (e: Exception) {
                // Handle any errors or exceptions here
                // For example, you can show an error message to the user
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


    suspend fun getJobs(): List<Job> {
        // Return the list of jobs
        return ApiService.getInstance().getJobs()
    }
}
