package com.mE.Health.models

import com.google.gson.annotations.SerializedName

data class DeleteAccountRequest(
    @SerializedName("reason")
    val reason: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("satisfaction_rating")
    val satisfactionRating: String,
    @SerializedName("additional_feedback")
    val additionalFeedback: String
)