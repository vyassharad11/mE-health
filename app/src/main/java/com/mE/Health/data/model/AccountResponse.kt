package com.mE.Health.data.model

import com.google.gson.annotations.SerializedName

data class AccountResponse(
    @SerializedName("mE_text_res")
    val mETextRes: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("data")
    val data: List<Reason>? = null
)

data class Reason(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("source")
    val source: String = ""
)