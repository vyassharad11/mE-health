package com.mE.Health.data.model.assist

import androidx.room.Entity
import androidx.room.PrimaryKey

data class AssistResponse(
    val mE_text_res: String,
    val data: List<AssistItem>,
    val status: Int
)

@Entity(tableName = "assist")
data class AssistItem(
    @PrimaryKey val id: String,
    val application: String,
    val category: String,
    val item: String,
    val default_activation_for_assist: Boolean,
    val frequency: String,
    val frequency_in_days: Int
)