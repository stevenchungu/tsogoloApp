package com.example.tsogolo.network

import com.example.tsogolo.model.PersonalityQuestion
import com.example.tsogolo.models.Job
import com.example.tsogolo.util.ApiConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET(ApiConstants.END_POINT)
    suspend  fun getPersonalityQuestions(): List<PersonalityQuestion>

    @GET(ApiConstants.JOBS_POINT)
    suspend  fun getJobs(): List<Job>

    companion object{
        private var apiService: ApiService?  = null
        fun getInstance() : ApiService{
            if (apiService == null){
                apiService = Retrofit.Builder()
                    .baseUrl(ApiConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}