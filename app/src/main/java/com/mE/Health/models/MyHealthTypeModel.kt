package com.mE.Health.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyHealthTypeModel(
    val name: String,
    val count: String,
    val icon: Int,
    val type: String = ""
)