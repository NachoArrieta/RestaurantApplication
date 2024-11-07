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

        if (selectedCity == context.getString(R.string.profile_rio_tercero)) {
            setValidDays(datePickerDialog, listOf(Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY))
        } else if (selectedCity == context.getString(R.string.profile_villa_del_dique)) {
            setValidDays(datePickerDialog, listOf(Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY))
        }

        datePickerDialog.show()
    }

    private fun setValidDays(datePickerDialog: DatePickerDialog, validDays: List<Int>) {
        datePickerDialog.datePicker.setOnDateChangedListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            if (dayOfWeek !in validDays) {
                Toast.makeText(context, R.string.reservations_day_not_valid, Toast.LENGTH_SHORT).show()
                calendar.set(Calendar.DAY_OF_WEEK, validDays.first())
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            }
        }
    }

}