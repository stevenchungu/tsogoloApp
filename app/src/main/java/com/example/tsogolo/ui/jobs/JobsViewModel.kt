package com.example.tsogolo.ui.jobs

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tsogolo.models.Job

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
        // Retrieve jobs from the repository or API
        val fetchedJobs = jobRepository.getJobs()
        allJobs = fetchedJobs
        _jobs.value = fetchedJobs
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
            _selectedCategories.value.contains(job.category)
        }
        _jobs.value = jobs.filter {
            it.title.contains(searchQuery.value, ignoreCase = true) ||
            it.company.contains(searchQuery.value, ignoreCase = true) ||
            it.location.contains(searchQuery.value, ignoreCase = true)
        }
    }

    fun getJob(jobId: String): Job {
        return allJobs.first { it.id == jobId }
    }
}

class JobRepository {
    // Simulated list of jobs
    private val jobs: List<Job> = listOf(
        Job("1", "Job Title 1", "Company A", "Location A", "2023-05-01", "https://example.com/image1.jpg"),
        Job("2", "Job Title 2", "Company B", "Location B", "2023-05-02", "https://example.com/image2.jpg"),
        Job("3", "Job Title 3", "Company C", "Location C", "2023-05-03", "https://example.com/image3.jpg"),
        Job("4", "Job Title 4", "Company D", "Location D", "2023-05-04", "https://example.com/image4.jpg"),
        Job("5", "Job Title 5", "Company E", "Location E", "2023-05-05", "https://example.com/image5.jpg")
    )

    fun getJobs(): List<Job> {
        // Return the list of jobs
        return jobs
    }
}
