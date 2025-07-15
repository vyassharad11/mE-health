package com.mE.Health.data.model

import com.google.gson.Gson

val gson = Gson()

fun Condition.toAssistDetail() = AssistDetailEntity(
    title = code_display ?: description ?: "Condition",
    date = createdAt,
    dateRange = if (createdAt != null && updatedAt != null) "$createdAt to $updatedAt" else null,
    source = "Condition",
    json = gson.toJson(this)
)

fun ImagingStudyEntity.toAssistDetail() = AssistDetailEntity(
    title = modality_display ?: description ?: "Imaging Study",
    date = createdAt,
    dateRange = if (createdAt != null && updatedAt != null) "$createdAt to $updatedAt" else null,
    source = "ImagingStudy",
    json = gson.toJson(this)
)

fun DiagnosticReport.toAssistDetail() = AssistDetailEntity(
    title = code_display ?: "Diagnostic Report",
    date = createdAt,
    dateRange = if (createdAt != null && updatedAt != null) "$createdAt to $updatedAt" else null,
    source = "DiagnosticReport",
    json = gson.toJson(this)
)

fun Observation.toAssistDetail() = AssistDetailEntity(
    title = code_display ?: description ?: "Observation",
    date = createdAt,
    dateRange = if (createdAt != null && updatedAt != null) "$createdAt to $updatedAt" else null,
    source = "Observation",
    json = gson.toJson(this)
)
