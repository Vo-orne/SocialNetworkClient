package com.example.myprofile.presentation.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * The `Parser` object provides utility functions for parsing date strings.
 */
object Parser {

    /**
     * Parses a date string into a `Date` object.
     *
     * @param input The date string to be parsed.
     * @return The parsed `Date` object if the input string is not blank; otherwise, returns `null`.
     */
    fun getDataFromString(input: String): Date? {
        return if (input.isNotBlank()) {
            SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault()).parse(input)
        } else {
            null
        }
    }
}