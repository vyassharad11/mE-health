package com.mE.Health.models

import com.google.gson.annotations.SerializedName

data class ProviderData(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val status: Boolean,
    @SerializedName("data")
    val data: List<ProviderDetail>? = null
)

data class ProviderDetail(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("practice_name")
    var practiceName: String? = null,
    @SerializedName("region")
    var region: String? = null,
    @SerializedName("city")
    var city: String? = null,
    @SerializedName("state")
    var state: String? = null,
    @SerializedName("country")
    var country: String? = null,
    @SerializedName("ehr_vendor")
    var ehrVendor: String? = null,
    @SerializedName("website")
    var website: String? = null,
    @SerializedName("logo_url")
    var logoUrl: String? = null,
    @SerializedName("connection_url")
    var connectionUrl: String? = null,
    @SerializedName("full_location")
    var fullLocation: String? = null,
)