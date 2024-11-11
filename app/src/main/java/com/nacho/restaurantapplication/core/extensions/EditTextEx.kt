package com.nacho.restaurantapplication.core.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.onTextChanged(listener: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            listener(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun EditText.loseFocusAfterAction(action: Int) {
    this.setOnEditorActionListener { v, actionId, _ ->
        if (actionId == action) {
            this.dismissKeyboard()
            v.clearFocus()
        }
        return@setOnEditorActionListener false
    }
}

fun EditText.formatAsCardNumber() {
    this.addTextChangedListener(object : TextWatcher {
        private var isUpdating = false
        private val space = "  "

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (isUpdating) return
            isUpdating = true

            s?.let {
                val digitsOnly = it.replace(Regex("[^\\d]"), "")
                val formatted = StringBuilder()

                for (i in digitsOnly.indices) {
                    formatted.append(digitsOnly[i])
                    if ((i + 1) % 4 == 0 && i < digitsOnly.length - 1) {
                        formatted.append(space)
                    }
                }

                val newFormattedText = formatted.toString()
                if (newFormattedText != it.toString()) {
                    setText(newFormattedText)
                    setSelection(newFormattedText.length)  // Coloca el cursor al final
                }
            }

            isUpdating = false
        }
    })
}

fun EditText.formatAsExpirationDate() {
    this.addTextChangedListener(object : TextWatcher {
        private var isUpdating = false

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (isUpdating) return
            isUpdating = true

            s?.let {
                val digitsOnly = it.replace(Regex("[^\\d]"), "")
                val formatted = StringBuilder()

                for (i in digitsOnly.indices) {
                    formatted.append(digitsOnly[i])
                    if (i == 1 && i < digitsOnly.length - 1) {
                        formatted.append("/")
                    }
                }

                val newFormattedText = formatted.toString()
                if (newFormattedText != it.toString()) {
                    setText(newFormattedText)
                    setSelection(newFormattedText.length)
                }
            }

            isUpdating = false
        }
    })
}