package com.example.tsogolo.models

data class Job(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val datePosted: String,
    val imageUrl: String,
    val description: String = "Job Description",
    val category: String = ""
) {
}
