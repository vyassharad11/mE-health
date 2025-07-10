package com.mE.Health.data.model

data class PractitionerOrganizationWithDetails(
    val id: String,
    val name: String,
    val telecom: String,
    val address: String
)

data class PractitionerBasicDetails(
    val practitionerName: String,
    val practitionerSpecialty: String,
    val organizationName: String
)