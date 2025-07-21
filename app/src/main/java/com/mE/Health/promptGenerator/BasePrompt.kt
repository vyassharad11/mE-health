package com.mE.Health.promptGenerator

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mE.Health.promptGenerator.PromptGenerator.dateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

open class BasePrompt {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

    fun extractText(json: String?): String {
        return parseJson(json)?.get("text")?.toString() ?: "N/A"
    }

    fun StringBuilder.appendSection(title: String, lines: List<String>) {
        append("\n$title:\n")
        lines.forEach { appendLine(it) }
    }

    fun extractDisplay(jsonString: String?, fallback: String?): String {
        return parseJson(jsonString)?.get("display")?.toString() ?: fallback ?: "Unknown"
    }

    fun formatDate(date: Date?): String {
        return date?.let { dateFormat.format(it) } ?: "N/A"
    }

    fun formatDate(input: Any?): String {
        return when (input) {
            is Date -> dateFormat.format(input)
            is String -> try {
                dateFormat.format(
                    SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss'Z'",
                        Locale.getDefault()
                    ).parse(input)!!
                )
            } catch (e: Exception) {
                "Invalid date"
            }

            else -> "N/A"
        }
    }

    fun parseJson(json: String?): Map<String, Any>? {
        if (json.isNullOrBlank()) return null
        return try {
            val type = object : TypeToken<Map<String, Any>>() {}.type
            Gson().fromJson<Map<String, Any>>(json, type)
        } catch (e: Exception) {
            null
        }
    }

}