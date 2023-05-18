package com.example.tsogolo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Category(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val categoryName: String? = null
) {



    override fun equals(other: Any?): Boolean {
        return other != null && other is Category && other.id == this.id
    }

    override fun hashCode(): Int {
        return id ?: 0
    }

}

