package com.mE.Health.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allergy_intolerance")
data class AllergyIntolerance(
    @PrimaryKey val id: String,
    val code_system: String?,
    val code: String?,
    val code_display: String?,
    val clinicalStatus: String?,
    val patientId: String?,
    val encounterId: String?,
    val recordedDate: String?,
    val createdAt: String?,
    val updatedAt: String?
)

@Entity(tableName = "appointment")
data class Appointment(
    @PrimaryKey val id: String,
    val startTime: String?,
    val endTime: String?,
    val status: String?,
    val patientId: String?,
    val practitionerId: String?,
    val practitionerName: String?,
    val practitionerSpecialty: String?,
    val encounterId: String?,
    val description: String?,
    val reasonCode: String?, // JSON string
    val createdAt: String?,
    val updatedAt: String?
)

data class ReasonCode(
    val system: String?,
    val code: String?,
    val display: String?
)

@Entity(tableName = "claim")
data class Claim(
    @PrimaryKey val claimId: String,
    val name: String?,
    val totalAmount: Double?,
    val totalCurrency: String?,
    val status: String?,
    val createdDate: String?,
    val insurance: String?, // JSON string
    val patientId: String?,
    val organizationId: String?,
    val createdAt: String?,
    val updatedAt: String?
)

data class Insurance(
    val sequence: Int?,
    val focal: Boolean?,
    val coverage: Result?
)

@Entity(tableName = "condition")
data class Condition(
    @PrimaryKey val id: String,
    val code_system: String?,
    val code: String?,
    val code_display: String?,
    val description: String?,
    val category: String?, // JSON string
    val clinicalStatus: String?,
    val onsetDate: String?,
    val recordedDate: String?,
    val patientId: String?,
    val encounterId: String?,
    val createdAt: String?,
    val updatedAt: String?
)

@Entity(tableName = "diagnostic_report")
data class DiagnosticReport(
    @PrimaryKey val id: String,
    val code_system: String?,
    val code: String?,
    val code_display: String?,
    val status: String?,
    val effectiveDate: String?,
    val issued: String?,
    val patientId: String?,
    val encounterId: String?,
    val performerId: String?,
    val organizationId: String?,
    val result: List<Result>?, // JSON array string
    val createdAt: String?,
    val updatedAt: String?
)

data class Result(
    val reference: String?,
    val display: String?
)

@Entity(tableName = "encounter")
data class Encounter(
    @PrimaryKey val id: String,
    val status: String?,
    val type_system: String?,
    val type_code: String?,
    val type_display: String?,
    val patientId: String?,
    val periodStart: String?,
    val periodEnd: String?,
    val practitionerId: String?,
    val organizationId: String?,
    val description: String?,
    val type: String?, // JSON string
    val createdAt: String?,
    val updatedAt: String?
)

@Entity(tableName = "immunization")
data class Immunization(
    @PrimaryKey val id: String,
    val vaccineCode_system: String?,
    val vaccineCode_code: String?,
    val vaccineCode_display: String?,
    val status: String?,
    val occurrenceDate: String?,
    val patientId: String?,
    val encounterId: String?,
    val createdAt: String?,
    val updatedAt: String?
)

@Entity(tableName = "medication_request")
data class MedicationRequest(
    @PrimaryKey val id: String,
    val medicationCode_system: String?,
    val medicationCode_code: String?,
    val medicationCode_display: String?,
    val medicationCode: String?,
    val description: String?,
    val status: String?,
    val authoredOn: String?,
    val dosageInstruction: String?, // JSON string
    val reasonCode: String?, // JSON string
    val patientId: String?,
    val encounterId: String?,
    val createdAt: String?,
    val updatedAt: String?
)

data class MedicationCode(
    val system: String?,
    val code: String?,
    val display: String?
)

data class DosageInstruction(
    val text: String?
)

@Entity(tableName = "observation")
data class Observation(
    @PrimaryKey val id: String,
    val code_system: String?,
    val code: String?,
    val code_display: String?,
    val category: String?, // JSON string
    val description: String?,
    val status: String?,
    val effectiveDate: String?,
    val value: String?, // JSON string
    val valueQuantity_value: Double?,
    val valueQuantity_unit: String?,
    val valueString: String?,
    val referenceRange_low: Double?,
    val referenceRange_high: Double?,
    val patientId: String?,
    val encounterId: String?,
    val createdAt: String?,
    val updatedAt: String?
)

data class Value(
    val value: String?,
    val unit: String?
)

@Entity(tableName = "organization")
data class Organization(
    @PrimaryKey val id: String,
    val name: String?,
    val telecom: String?,
    val address: String?
)

@Entity(tableName = "patient")
data class Patient(
    @PrimaryKey val id: String,
    val name: String?,
    val birthDate: String?,
    val gender: String?,
    val address: String?, // JSON string
    val telecom: String?, // JSON string (semicolon separated)
    val maritalStatus: String?,
    val identifier: String?, // JSON string
    val communication: String?, // JSON string
    val createdAt: String?,
    val updatedAt: String?
)

data class Address(
    val line: List<String>?,
    val city: String?,
    val state: String?,
    val postalCode: String?
)

data class Telecom(
    val system: String?,
    val value: String?
)

data class Communication(
    val language: Language?
)

data class Language(
    val coding: List<ReasonCode>?
)


@Entity(tableName = "practitioner")
data class Practitioner(
    @PrimaryKey val id: String,
    val name: String?,
    val specialty: String?,
    val telecom: String?, // JSON string (semicolon separated)
    val qualification: String?, // JSON string
    val createdAt: String?,
    val updatedAt: String?
)

data class Qualification(
    val code: Code?
)

data class Code(
    val coding: List<ReasonCode>?,
    val text: String?
)

@Entity(tableName = "practitioner_organization")
data class PractitionerOrganization(
    @PrimaryKey val id: String,
    val practitionerId: String?,
    val organizationId: String?
)

@Entity(tableName = "procedure")
data class Procedure(
    @PrimaryKey val id: String,
    val code_system: String?,
    val code: String?,
    val code_display: String?,
    val status: String?,
    val performedDate: String?,
    val reasonCode: String?, // JSON string
    val patientId: String?,
    val encounterId: String?,
    val createdAt: String?,
    val updatedAt: String?
)

@Entity(tableName = "imaging")
data class Imaging(
    @PrimaryKey val id: String,
    val status: String?,
    val modality_code: String?,
    val modality_display: String?,
    val started: String?,
    val description: String?,
    val reasonCode_display: String?,
    val reasonCode: String?,
    val procedureCode_display: String?,
    val performer: String?,
    val note: String?
)