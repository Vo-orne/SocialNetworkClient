package com.example.myprofile.presentation.utils

import android.util.Patterns
import com.example.myprofile.R

/**
 * The `Validation` object provides utility functions for validating passwords and email addresses.
 */
object Validation {

    /**
     * Validates a password based on certain criteria.
     *
     * @param password The password string to be validated.
     * @return An error message resource ID if the password is invalid; otherwise, returns `null`.
     */
    fun isValidPassword(password: String): String? {
        var isHasEnoughLength = false
        var isHasNumber = false
        var isHasLetter = false

        // Check if the password has enough length
        if (password.length >= Constants.MIN_PASSWORD_SIZE) {
            isHasEnoughLength = true
        }

        // Check if the password contains at least one number
        if (password.contains(Regex("\\d+"))) isHasNumber = true

        // Check if the password contains at least one letter
        if (password.contains(Regex("[a-zA-Z]+"))) isHasLetter = true

        // Return appropriate error message based on validation
        return when {
            !isHasEnoughLength -> R.string.error_password_is_short.toString()
            !isHasNumber -> R.string.error_password_without_numbers.toString()
            !isHasLetter -> R.string.error_password_without_letters.toString()
            else -> null
        }
    }

    /**
     * Validates an email address format.
     *
     * @param email The email address string to be validated.
     * @return `true` if the email address is valid; otherwise, returns `false`.
     */
    fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
