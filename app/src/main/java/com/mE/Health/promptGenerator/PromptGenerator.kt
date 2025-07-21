package com.mE.Health.promptGenerator

import com.mE.Health.data.model.Condition
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.ImagingStudyEntity
import com.mE.Health.data.model.Observation
import com.mE.Health.data.model.Procedure

enum class DateRange {
    LastYear,
    AllTime
}

object PromptGenerator : BasePrompt() {

    fun generateChronicConditionPrompt(
        vitals: List<Observation>,
        labResults: List<DiagnosticReport>,
        conditions: List<Condition>,
        imagingStudies: List<ImagingStudyEntity>,
        dateRange: DateRange
    ): String = buildString {
        append("Identify potential undiagnosed chronic conditions for a patient within ${if (dateRange == DateRange.LastYear) "the last year" else "all time"}:\n\n")

        appendSection("Vitals", vitals.map { vital ->
            val name = extractDisplay(vital.code, vital.code_display)
            val value = "${vital.valueQuantity_value} ${vital.valueQuantity_unit ?: "N/A"}"
            val date = formatDate(vital.effectiveDate)
            "- $name: $value, Date: $date"
        })

        appendSection("Lab Results", labResults.map { lab ->
            val resultJson = lab.result.toString()
            val name = extractDisplay(lab.code, lab.code_display)
            val value = parseJson(resultJson)?.get("value")?.toString() ?: "N/A"
            val date = formatDate(lab.effectiveDate)
            "- $name: $value, Date: $date"
        })

        appendSection("Conditions", conditions.map { condition ->
            val name = extractDisplay(condition.code, condition.description)
            val status = condition.clinicalStatus ?: "N/A"
            val onset = formatDate(condition.onsetDate)
            "- $name, Status: $status, Onset: $onset"
        })

        appendSection("Imaging Studies", imagingStudies.map { study ->
            val modality = extractDisplay(study.modality, null)
            val description = study.description ?: "Unknown"
            val note = parseJson(study.note)?.get("text")?.toString() ?: "N/A"
            val date = formatDate(study.started)
            "- Study: $description, Modality: $modality, Date: $date, Note: $note"
        })

        append("\nTask: Identify potential undiagnosed conditions based on vitals, lab results, and imaging findings. Provide a condition name, confidence score (0.0–1.0), and recommendation in JSON format.")
    }

    fun generateImagingValidationPrompt(
        imagingStudies: List<ImagingStudyEntity>,
        conditions: List<Condition>,
        procedures: List<Procedure>,
        dateRange: DateRange
    ): String = buildString {
        val dateLabel = if (dateRange == DateRange.LastYear) "the last year" else "all time"

        append("Validate chronic conditions using the following imaging data, conditions, and procedures for a patient within $dateLabel:\n\n")

        appendSection("Imaging Studies", imagingStudies.map { study ->
            val modality = extractDisplay(study.modality, null)
            val description = study.description ?: "Unknown"
            val note = extractText(study.note)
            val date = formatDate(study.started)
            "- Study: $description, Modality: $modality, Date: $date, Note: $note"
        })

        appendSection("Conditions", conditions.map { condition ->
            val name = extractDisplay(condition.code, condition.description)
            val status = condition.clinicalStatus ?: "N/A"
            val onset = formatDate(condition.onsetDate)
            "- Condition: $name, Status: $status, Onset: $onset"
        })

        appendSection("Procedures", procedures.map { procedure ->
            val name = extractDisplay(procedure.code, null)
            val date = formatDate(procedure.performedDate)
            "- Procedure: $name, Date: $date"
        })

        append("\nTask: Validate each condition using imaging findings. Provide a description, confidence score (0.0–1.0), and any recommendations. Return results in JSON format.")
    }


}
