package com.example.tsogolo.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["personalityId", "careerId"],
    foreignKeys = [
        ForeignKey(
            entity = Personality::class,
            parentColumns = ["id"],
            childColumns = ["personalityId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Career::class,
            parentColumns = ["id"],
            childColumns = ["careerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class PersonalitySuitableCareer(
    var personalityId: Int,
    var careerId: Int,
)