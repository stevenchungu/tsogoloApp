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
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import java.net.SocketTimeoutException

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
                    Log.d("TAG", "getInstance: josh ")
                    if (users.isNotEmpty()) {
                        Log.d("TAG", "getInstance: josh1 ")
                        if (activeUserId == null) {
                            Log.d("TAG", "getInstance: josh2 ")
                            activeUser.value = users[0]
                            activeUserId = activeUser.value.id
                        } else {
                            activeUser.value = users.first { it.id == activeUserId }
                        }
                        activePersonalities.value = db.personalityDao().getAllOf(activeUser.value.id!!)
                        Log.d("TAG", "getInstance: josh$activePersonalities} ")
                        if (activePersonalities.value.isNotEmpty()) {
                            Log.d("TAG", "getInstance: josh$activePersonalities} ")
                            val fetchedJobs = jobRepository.getJobs(activePersonalities.value[0].type!!) // Pass the personalityType parameter here
                            Log.d("TAG", "getInstance: joshPERSON$fetchedJobs} ")

                            while (fetchedJobs.isEmpty()){
                                Log.d("TAG", "getInstance: josh$activePersonalities} ")
                                val fetchedJobs = jobRepository.getJobs(activePersonalities.value[0].type!!) // Pass the personalityType parameter here
                                Log.d("TAG", "getInstance: joshPERSON$fetchedJobs} ")
                            }

                            MainScope().launch {
                                Log.d("TAG", "getInstance: joshs ")
                                allJobs = fetchedJobs
                                _jobs.value = fetchedJobs
                                _isLoading.value = false
                                Log.d("TAG", "getInstance: joshs ")
                            }
                            Log.d("Joblessss", activePersonalities.value[0].type!!)
                            Log.d("Joblesize", fetchedJobs.size.toString())


                        } else {
                            val fetchedJobs = jobRepository.getJobs("INTJ") // Pass the personalityType parameter here
                            MainScope().launch {
                                Log.d("TAG", "getInstance: josh ")
                                allJobs = fetchedJobs
                                _jobs.value = fetchedJobs
                                Log.d("TAG", "getInstance: josh ")
                                _isLoading.value = false

                            }
                            Log.d("TAG", "getInstance: joshss ")
                        }
                    }
                    else {
                        val fetchedJobs = jobRepository.getJobs("INTJ") // Pass the personalityType parameter here
                        MainScope().launch {
                            allJobs = fetchedJobs
                            _jobs.value = fetchedJobs
                            Log.d("TAG", "getInstance: joshe ")
                            _isLoading.value = false
                            Log.d("TAG", "getInstance: joshes ")

                        }
                    }
                }



            }catch (e: SocketTimeoutException) {
                // Handle timeout exception
                // Show an error message to the user or provide a retry mechanism
                Log.e("Jobs", "Errors", e)
            } catch (e: Exception) {
                // Handle any errors or exceptions here
                // For example, you can show an error message to the user
//                Log.e("Jobs", "Error", e)
            }
            finally {
                MainScope().launch {
                    _isLoading.value = false
                    Log.d("isLoading...", _isLoading.value.toString())
                }

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