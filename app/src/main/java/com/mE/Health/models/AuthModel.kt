package com.mE.Health.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AuthModel(
    @SerializedName("authorization_endpoint")
    val authorizationEndpoint: String? = null,
    @SerializedName("token_endpoint")
    val tokenEndpoint: String? = null
)