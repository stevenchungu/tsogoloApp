package com.example.tsogolo.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["programId", "subjectId"],
    foreignKeys = [
        ForeignKey(
            entity = Program::class,
            parentColumns = ["id"],
            childColumns = ["programId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Subject::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class ProgramRequirement(
    var programId: Int,
    var subjectId: Int,
    var grade: Int = 2,
)