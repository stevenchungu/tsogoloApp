package com.example.tsogolo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Program(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var name: String? = null,
    var duration: Int = 4
) {
    override fun equals(other: Any?): Boolean {
        return other != null && other is Program && other.id == this.id
    }

    override fun hashCode(): Int {
        return id ?: 0
    }
}