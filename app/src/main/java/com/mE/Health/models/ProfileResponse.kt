package com.mE.Health.models

data class ProfileResponse(
    val data: ProfileData,
    val message: String,
    val status: Int,
    val total_address: Int
)

data class ProfileData(
    val first_name: String?,
    val last_name: String?,
    val email: String?,
    val phoneNumber: String?,
    val countryCode: String?,
    val address: String?,
    val gender: String?,
    val isMarried: Boolean?,
    val dateOfBirth: String?
    // Add other fields if needed later
)
