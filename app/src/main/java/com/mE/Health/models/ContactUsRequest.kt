package com.mE.Health.models

import com.google.gson.annotations.SerializedName

data class ContactUsRequest(
    @SerializedName("user")
    val user: String,
    @SerializedName("first_name")
    val first_name: String,
    @SerializedName("last_name")
    val last_name: String,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("message")
    val message: String
)