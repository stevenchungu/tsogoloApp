package com.example.tsogolo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Personality(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var type: String? = INTJ,
    var description: String? = null,
) {
    companion object PersonalityType {
        const val INTJ = "INTJ"
        const val INTP = "INTP"
        const val INFJ = "INFJ"
        const val INFP = "INFP"
        const val ISTJ = "ISTJ"
        const val ISTP = "ISTP"
        const val ISFJ = "ISFJ"
        const val ISFP = "ISFP"
        const val ENTJ = "ENTJ"
        const val ENTP = "ENTP"
        const val ENFJ = "ENFJ"
        const val ENFP = "ENFP"
        const val ESTJ = "ESTJ"
        const val ESTP = "ESTP"
        const val ESFJ = "ESFJ"
        const val ESFP = "ESFP"
    }
}