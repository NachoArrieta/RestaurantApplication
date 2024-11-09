package com.nacho.restaurantapplication.core.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import com.nacho.restaurantapplication.R
import java.util.Calendar

class DatePickerHelper(
    private val context: Context,
    private val onDateSelected: (String) -> Unit
) {

    fun showDatePicker(selectedCity: String) {
        val calendar = Calendar.getInstance()
        val today = calendar.timeInMillis

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = today

        datePickerDialog.datePicker.setOnDateChangedListener { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)
            val dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK)

            val validDays = when (selectedCity) {
                context.getString(R.string.profile_rio_tercero) -> listOf(Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY)
                context.getString(R.string.profile_villa_del_dique) -> listOf(Calendar.THURSDAY,Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY)
                else -> emptyList()
            }

            if (dayOfWeek !in validDays) {
                Toast.makeText(context, R.string.reservations_day_not_valid, Toast.LENGTH_SHORT).show()
                selectedCalendar.set(Calendar.DAY_OF_WEEK, validDays.first())
                datePickerDialog.updateDate(selectedCalendar.get(Calendar.YEAR), selectedCalendar.get(Calendar.MONTH), selectedCalendar.get(Calendar.DAY_OF_MONTH))
            }
        }

        datePickerDialog.show()
    }

}