package com.mE.Health.utility

import com.mE.Health.data.model.ContactInfo
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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

fun String.toDisplayDateTime(): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.ENGLISH)
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

fun String.capitalFirstChar():String{
    val capitalized = this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
    return capitalized
}

fun String.formatIntoPrettyDate(): String {
    val zonedDateTime = ZonedDateTime.parse(this)
    val localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault())

    val outputFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a")

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
        return Pair("","")
    }
}