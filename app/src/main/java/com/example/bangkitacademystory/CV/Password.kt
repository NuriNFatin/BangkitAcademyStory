package com.example.bangkitacademystory.CV

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class Password @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatEditText(context, attrs) {

    init {
        addTextChangedListener(PasswordTextWatcher())
    }


    private inner class PasswordTextWatcher : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
            val password = charSequence.toString()

            when {
                password.length < 8 -> {
                    error = "Password must be at least 8 characters"
                }
                else -> {
                    error = null
                }
            }
        }

        override fun afterTextChanged(editable: Editable) {
        }
    }
}