package com.example.tsogolo.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["collegeId", "programId"],
    foreignKeys = [
        ForeignKey(
            entity = Program::class,
            parentColumns = ["id"],
            childColumns = ["programId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = College::class,
            parentColumns = ["id"],
            childColumns = ["collegeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class CollegeProgram(
    var programId: Int,
    var collegeId: Int,
)