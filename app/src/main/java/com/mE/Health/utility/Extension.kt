package com.mE.Health.utility

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.mE.Health.data.model.ContactInfo
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun String.extractContactInfo(): ContactInfo {
    val parts = this.split(";")
    var phone: String? = null
    var email: String? = null

    for (part in parts) {
        when {
            part.contains("@") -> email = part.trim()
            part.matches(Regex("[+\\d\\s\\-()]+")) -> phone = part.trim()
        }
    }

    return ContactInfo(phone, email)
}

fun String.toFormattedDate(): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        // In case the date string is not in expected format
        ""
    }
}

fun String.toDisplayDate(): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        // In case the date string is not in expected format
        ""
    }
}

fun String.toDisplayDateTime(): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, hh:mm a", Locale.ENGLISH)
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        ""
    }
}

// Extension function to convert ISO string to formatted time only
fun String.toDisplayTime(): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        ""
    }
}

fun String.capitalFirstChar(): String {
    val capitalized = this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
    return capitalized
}

fun String.formatIntoPrettyDate(): String {
    val zonedDateTime = ZonedDateTime.parse(this)
    val localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault())

    val outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mm a")

    return localDateTime.format(outputFormatter)
}


fun openCloseTime(startTime: String?, endTime: String?): Pair<String, String> {
    if (startTime != null && endTime != null) {
        val startDateTime = startTime.toDisplayDateTime()
        val endTimeFormatted = endTime.toDisplayTime()

        // Split the startDateTime to separate date and time
        val parts = startDateTime.split(", ")
        val datePart = parts[0]       // Example: 10 Sep 2022
        val startTimePart = parts[1]  // Example: 09:30 AM

        // Final formatted string with new line
        //return "$datePart,\n$startTimePart - $endTimeFormatted"

        return Pair(datePart, "$startTimePart - $endTimeFormatted")
    } else {
        return Pair("", "")
    }
}

fun String.dateToLocalDate(format: String? = null): LocalDate {
    return if (format == null) {
        // Try ISO instant or ISO offset
        OffsetDateTime.parse(this).toLocalDate()
    } else {
        val formatter = DateTimeFormatter.ofPattern(format, Locale.getDefault())
        LocalDate.parse(this, formatter)
    }
}

@Throws(JsonSyntaxException::class)
fun <T> fromJson(json: String?, classOfT: Class<T>): T {
    return  Gson().fromJson(json, TypeToken.get(classOfT))
}

fun String.toFormateCalendar(format: String,sdf: SimpleDateFormat): Calendar {
    return try {
        val zonedDateTime = ZonedDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH)
        zonedDateTime.format(formatter).getCalendarFromString(sdf)
    } catch (e: Exception) {
        Calendar.getInstance()
    }
}

fun String.getCalendarFromString(sdf: SimpleDateFormat): Calendar {
    val calendar = Calendar.getInstance()
    try {
        calendar.time = sdf.parse(this)!!
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return calendar
}