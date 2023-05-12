package com.example.tsogolo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Career(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var title: String? = null,
    var description: String? = null,
    var aas: Double = 0.0
) {

    override fun equals(other: Any?): Boolean {
        return other != null && other is Career && other.id == this.id
    }

    override fun hashCode(): Int {
        return id ?: 0
    }
}