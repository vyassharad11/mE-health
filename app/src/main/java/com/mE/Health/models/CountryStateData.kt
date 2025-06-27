package com.mE.Health.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CountryStateData(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val status: Boolean,
    @SerializedName("data")
    val data: ByCountryState? = null
)

data class ByCountryState(
    @SerializedName("total_practices")
    var totalPractices: Int? = null,
    @SerializedName("by_country")
    var byCountry: ArrayList<CountryState>? = null,
    @SerializedName("top_states")
    var topStates: ArrayList<CountryState>? = null
)

data class CountryState(
    @SerializedName("state")
    var state: String? = null,
    @SerializedName("country")
    var country: String? = null,
    @SerializedName("count")
    var count: String? = null,
    @SerializedName("logo")
    var logo: String? = null,
    @SerializedName("country_logo")
    var countryLogo: Int? = null
)