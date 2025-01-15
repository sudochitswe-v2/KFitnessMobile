package com.kmd.kfitness.general.common

import android.text.Editable
import android.text.TextWatcher

object KTextWatcher {
    fun createAfterTextChangedListener(onAfterTextChanged: (Editable) -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Ignore
            }

            override fun afterTextChanged(s: Editable) {
                onAfterTextChanged(s) // Invoke the passed lambda
            }
        }
    }
}