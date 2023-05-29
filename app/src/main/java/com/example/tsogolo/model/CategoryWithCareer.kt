package com.example.tsogolo.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class CareerCategoryWithCareerAndCategory(
    @Embedded val careerCategory: CareerCategory,
    @Relation(
        parentColumn = "careerId",
        entityColumn = "id"
    )
    val career: List<Career>,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: List<Category>
)
