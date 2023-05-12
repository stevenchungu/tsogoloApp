package com.example.tsogolo.database

import com.example.tsogolo.model.Personality

data class CareerAlt(
    val ca_id: Int? = null,
    var title: String? = null,
    var ca_description: String? = null,
    var aas: Double = 0.0
)

class PersonalityAlt(
    val pe_id: Int? = null,
    var type: String? = Personality.INTJ,
    var pe_description: String? = null,
)

class ProgramAlt(
    val pr_id: Int? = null,
    var name: String? = null,
    var duration: Int = 4
)