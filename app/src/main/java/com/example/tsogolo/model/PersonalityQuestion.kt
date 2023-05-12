package com.example.tsogolo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PersonalityQuestion(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var question: String? = null,
    var agreeType: String = I.toString(),
    var denialType: String = E.toString()
) {
    companion object Type {
        const val I = 'I'
        const val E = 'E'
        const val N = 'N'
        const val S = 'S'
        const val T = 'T'
        const val F = 'F'
        const val J = 'J'
        const val P = 'P'
    }
}