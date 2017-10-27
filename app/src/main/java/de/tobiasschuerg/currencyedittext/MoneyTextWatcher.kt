package de.tobiasschuerg.currencyedittext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference
import java.text.NumberFormat
import java.util.*

class MoneyTextWatcher(editText: EditText, locale: Locale) : TextWatcher {

    private val editTextWeakReference: WeakReference<EditText>
    private val currencyInstance = NumberFormat.getCurrencyInstance(locale)


    init {
        editTextWeakReference = WeakReference(editText)
        currencyInstance.maximumFractionDigits = 0
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable) {
        val editText = editTextWeakReference.get() ?: return
        editText.removeTextChangedListener(this)
        val s = editable.toString()
        val cleanString = s.replace("[\\D]".toRegex(), "")

        if (cleanString.isNotBlank()) {
            var parsed: Long? = cleanString.toLong()
            if (parsed == null) {
                parsed = 0
            }

            val currentSelection = editText.selectionStart

            val formatted = currencyInstance.format(parsed)
            editText.setText(formatted)

            val indexOfLastDigit = indexOfLastDigit(formatted)
            val indexOfFirstDigit = indexOfFirstDigit(formatted)
            if (currentSelection > indexOfLastDigit || currentSelection < indexOfFirstDigit) {
                editText.setSelection(indexOfLastDigit + 1)
            } else {
                editText.setSelection(currentSelection + 1)
            }
        }

        editText.addTextChangedListener(this)
    }

    private fun indexOfLastDigit(string: String, index: Int = string.lastIndex): Int {
        if (index == 0 || string[index].isDigit()) {
            return index
        } else {
            return indexOfLastDigit(string, index - 1)
        }
    }

    private fun indexOfFirstDigit(string: String, index: Int = 0): Int {
        if (index == string.length || string[index].isDigit()) {
            return index
        } else {
            return indexOfLastDigit(string, index + 1)
        }
    }

}