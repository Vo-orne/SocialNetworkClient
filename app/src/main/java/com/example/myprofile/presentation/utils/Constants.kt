package com.example.myprofile.presentation.utils

/**
 * The `Constants` object contains constants used in the application.
 * All variables in this object are considered constant and do not change during the program's execution.
 */
object Constants {

    const val SHARED_PREFERENCES_KEY = "login_data"
    const val EMAIL_KEY = "email"
    const val PASSWORD_KEY = "password"

    // Maximum length of a password.
    const val MIN_PASSWORD_SIZE = 8

    // String indicating that the password is correct.
    const val PASSWORD_IS_CORRECT = "Password correct."

    // The base URL of the server
    const val BASE_URL = "http://178.63.9.114:7777/api/"

    // Authorization prefix.
    const val AUTHORIZATION_PREFIX = "Bearer"

    // String command to open the add contact dialog.
    const val EDIT_USER_DIALOG = "EditProfileDialog"

    // The dates formats
    const val DATE_FORMAT = "dd/MM/yyyy"
    const val INPUT_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy"
}