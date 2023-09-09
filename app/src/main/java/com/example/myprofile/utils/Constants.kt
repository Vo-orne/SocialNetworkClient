package com.example.myprofile.utils

/**
 * The `Constants` object contains constants used in the application.
 * All variables in this object are considered constant and do not change during the program's execution.
 */
object Constants {
    // Key for storing login data in shared preferences.
    const val SHARED_PREFERENCES_KEY = "login_data"

    // Key for storing the user's name in shared preferences.
    const val USER_NAME_KEY = "userName"

    // Key for storing the autoLogin in shared preferences.
    const val AUTO_LOGIN_KEY = "autoLogin"

    // Maximum length of a password.
    const val MAX_PASSWORD_SIZE = 8 // TODO: bad naming

    // String indicating that the password is correct.
    const val PASSWORD_IS_CORRECT = "Password correct."
}
