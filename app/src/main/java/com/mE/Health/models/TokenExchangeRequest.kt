package com.mE.Health.models

import com.google.gson.annotations.SerializedName

data class TokenExchangeRequest(
    @SerializedName("grant_type")
    val grantType: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("redirect_uri")
    val redirectURI: String,
    @SerializedName("client_id")
    val clientId: String ,
    @SerializedName("code_verifier")
    val codeVerifier: String
)