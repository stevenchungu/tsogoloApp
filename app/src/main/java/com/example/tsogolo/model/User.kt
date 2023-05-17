package com.example.tsogolo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class User(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var name: String? = null,
    var dob: Date? = null,
    var eduLevel: String = MSCE
) {
    companion object EduLevel {
        const val PLSCE = "PLSCE"
        const val MSCE = "MSCE"
    }

    /**
     * Builder
     */
    class Builder {
        private val user = User()

        fun setName(name: String?): Builder {
            user.name = name
            return this
        }

        fun setDob(dob: Date?): Builder {
            user.dob = dob
            return this
        }

        fun setEduLevel(eduLevel: String): Builder {
            user.eduLevel = eduLevel
            return this
        }

        fun build() : User = user
    }
}