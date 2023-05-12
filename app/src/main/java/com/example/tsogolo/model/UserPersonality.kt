package com.example.tsogolo.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["userId", "personalityId"],
    foreignKeys = [
        ForeignKey(
            entity = Personality::class,
            parentColumns = ["id"],
            childColumns = ["personalityId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class UserPersonality(
    var userId: Int,
    var personalityId: Int,
    var rank: Int = 0
)