package com.mE.Health.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assist_detail")
data class AssistDetailEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val date: String?,
    val dateRange: String?,
    val source: String,
    val json: String
)