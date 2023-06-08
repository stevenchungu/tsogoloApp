package com.example.tsogolo.models

data class Job(
    val id: Int,
    val title: String,
    val sector: String,
    val location: String,
    val time: String,
    val summary: String = "Job Description",
    val company: String

) : java.io.Serializable
