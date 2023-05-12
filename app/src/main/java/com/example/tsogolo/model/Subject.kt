package com.example.tsogolo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Subject(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var name: String? = null,
    var category: String = SCIENCE,
) {
    companion object Category {
        const val SCIENCE = "Science"
        const val HUMANITY = "Humanity"
        const val LANGUAGE = "Language"
    }
}