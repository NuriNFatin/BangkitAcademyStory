package com.example.bangkitacademystory.CV

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import java.util.regex.Pattern

class Email @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatEditText(context, attrs) {

    private val emailPattern: Pattern = Pattern.compile(
        "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    )

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val isValid = isValidEmail(s)
                error = if (!isValid) "Invalid Email" else null
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return emailPattern.matcher(email).matches()
    }
}