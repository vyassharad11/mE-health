package com.mE.Health.data.model

import com.google.gson.annotations.SerializedName

data class DeleteAccountResponse(
    @SerializedName("mE_text_res")
    val mETextRes: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("detail")
    val detail: String
)

data class ContactUsResponse(
    @SerializedName("mE_text_res")
    val mETextRes: String?,
    @SerializedName("status")
    val status: String = "",
    @SerializedName("detail")
    val detail: String
)

data class MeText(
    @SerializedName("address")
    val address: String = ""
)
