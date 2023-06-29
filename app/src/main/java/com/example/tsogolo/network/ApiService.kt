package com.example.tsogolo.network

import android.util.Log
import com.example.tsogolo.model.PersonalityQuestion
import com.example.tsogolo.models.Job
import com.example.tsogolo.util.ApiConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {

    fun baseUrl(): String // Add the baseUrl() method

    @GET(ApiConstants.END_POINT)
    suspend fun getPersonalityQuestions(): List<PersonalityQuestion>

    @GET(ApiConstants.JOBS_POINT)
    suspend fun getJobs(@Query("personalityType") personalityType: String): List<Job> // Add the @Query annotation here


    companion object {
        private var apiService: ApiService? = null

        fun getInstance(): ApiService {
            if (apiService == null) {
                val okHttpClient = OkHttpClient.Builder()
                    .callTimeout(30, TimeUnit.SECONDS) // Set the timeout duration here (e.g., 30 seconds)
                    .build()

//                Log.d("TAG", "getInstance: josh ")

                apiService = Retrofit.Builder()
                    .baseUrl(ApiConstants.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)

//                apiService = Retrofit.Builder()
//                    .baseUrl(ApiConstants.BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//                    .create(ApiService::class.java)
            }

//            Log.d("TAG", "API Service instance created - Base URL: ${apiService?.baseUrl()}")


            return apiService!!
        }
    }
}