package com.mE.Health.models

import com.google.gson.annotations.SerializedName

data class UserDataResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("data")
    val data: Data? = null
)

data class Data(
    @SerializedName("user_id")
    var userId: String? = null,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("user_type")
    var userType: String? = null,

    @SerializedName("phone")
    var phone: String? = null,

    @SerializedName("countryCode")
    var countryCode: String? = null,

    @SerializedName("token")
    var token: String? = null,

    @SerializedName("auth_token")
    var authToken: String? = null,

    @SerializedName("first_name")
    var firstName: String? = null,

    @SerializedName("last_name")
    var lastName: String? = null,

    @SerializedName("walletAddress")
    var walletAddress: String? = null,

    @SerializedName("provider_id")
    var providerId: String? = null,

    @SerializedName("isPhoneVerified")
    var isPhoneVerified: Boolean? = null,

    @SerializedName("isEmailVerified")
    var isEmailVerified: Boolean? = null
)