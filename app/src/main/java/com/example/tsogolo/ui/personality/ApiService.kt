package com.example.tsogolo.ui.personality


import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("personality-questions")
    fun storePersonalityQuestions(@Body questions: RequestBody): Call<Void>
}