package com.mE.Health.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("firebase_token")
    val firebase_token: String,
    @SerializedName("user_type")
    val user_type: String
)