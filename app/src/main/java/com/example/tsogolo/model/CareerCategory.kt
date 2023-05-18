package com.example.tsogolo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    primaryKeys = ["careerID", "categoryID"],
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryID"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = Career::class,
            parentColumns = ["id"],
            childColumns = ["careerID"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
class CareerCategory(
    @ColumnInfo(name = "careerID") var careerID: Int,
    @ColumnInfo(name = "categoryID") var categoryID: Int
//    var careerID: Int,
//    var categoryID: Int
)

