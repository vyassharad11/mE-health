package com.mE.Health.utility

import android.app.DatePickerDialog
import android.content.Context
import com.mE.Health.R
import java.util.Calendar

object Utils {

    fun showDatePicker(
        context: Context,
        calendar: Calendar,
        minDate: Long? = null,
        maxDate: Long? = null,
        onDatePicked: (day: Int, month: Int, year: Int) -> Unit,
        onCancel: () -> Unit = {}
    ) {
        val datePickerDialog = DatePickerDialog(
            context,
            R.style.my_dialog_theme,
            { _, year, monthOfYear, dayOfMonth ->
                onDatePicked(dayOfMonth, monthOfYear, year)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        minDate?.let { datePickerDialog.datePicker.minDate = it }
        maxDate?.let { datePickerDialog.datePicker.maxDate = it }

        datePickerDialog.setOnCancelListener {
            onCancel.invoke()
        }

        datePickerDialog.setOnDismissListener {

        }

        datePickerDialog.show()
    }

    fun getCurrentTimeMillisPlusDays(daysToAdd: Int = 0): Long {
        val currentTimeMillis = System.currentTimeMillis()
        val millisToAdd = daysToAdd * 24 * 60 * 60 * 1000L
        return currentTimeMillis + millisToAdd
    }

}