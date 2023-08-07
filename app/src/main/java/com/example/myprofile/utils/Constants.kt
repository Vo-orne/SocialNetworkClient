package com.example.myprofile.utils

/**
 * The `Constants` object contains constants used in the application.
 * All variables in this object are considered constant and do not change during the program's execution.
 */
object Constants {
    // Key for storing login data in shared preferences.
    const val SHARED_PREFERENCES_KEY = "login_data"

    // Key for storing the user's email in shared preferences.
    const val EMAIL_KEY = "email"

    // Key for storing the user's password in shared preferences.
    const val PASSWORD_KEY = "password"

    // Key for storing the user's name in shared preferences.
    const val USER_NAME_KEY = "userName"

    // Maximum length of a password.
    const val MAX_PASSWORD_SIZE = 8 // TODO: bad naming

    // String indicating that the password is correct.
    const val PASSWORD_IS_CORRECT = "Password correct."
}
