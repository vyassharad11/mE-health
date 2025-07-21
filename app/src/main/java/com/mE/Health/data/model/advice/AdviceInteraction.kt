package com.mE.Health.data.model.advice

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AdviceInteraction(
    @PrimaryKey(autoGenerate = true) val adviceId: Int = 0,
    val userId: String,
    val assistId: String,
    val adviceDate: String,
    val title: String,
    val advice: String,
    val like: Boolean? = null,
    val review: String? = null,
    val ignore: Boolean? = null,
    val nextAdvice: String? = null
)