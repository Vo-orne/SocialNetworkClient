package com.example.myprofile.utils

import android.util.Patterns
import com.example.myprofile.R

object Validation {

    fun isValidPassword(password: String): String? {
        var isHasEnoughLength = false
        var isHasNumber = false
        var isHasLetter = false

        if (password.length >= Constants.MAX_PASSWORD_SIZE) {
            isHasEnoughLength = true
        }

        if (password.contains(Regex("\\d+"))) isHasNumber = true
        if (password.contains(Regex("[a-zA-Z]+"))) isHasLetter = true

        return when {
            !isHasEnoughLength -> R.string.error_password_is_short.toString()
            !isHasNumber -> R.string.error_password_without_numbers.toString()
            !isHasLetter -> R.string.error_password_without_letters.toString()
            else -> null
        }
    }

    fun isValidEmail(email: String) : Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

}