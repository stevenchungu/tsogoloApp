package com.example.tsogolo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class College(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var name: String? = null,
    var isPublic: Boolean = true,
    var isAccredited: Boolean = true
) {

    override fun equals(other: Any?): Boolean {
        return other != null && other is College && other.id == this.id
    }

    override fun hashCode(): Int {
        return id ?: 0
    }
}