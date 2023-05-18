package com.example.tsogolo.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class CategoryWithCareer(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CareerCategory::class,
            parentColumn = "categoryID",
            entityColumn = "careerID"
        )
    )
    val careers: List<Career>
)
