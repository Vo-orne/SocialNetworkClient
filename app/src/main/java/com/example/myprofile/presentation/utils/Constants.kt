package com.example.myprofile.presentation.utils

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
    const val MIN_PASSWORD_SIZE = 8

    // String indicating that the password is correct.
    const val PASSWORD_IS_CORRECT = "Password correct."

    // String command to open the add contact dialog.
    const val ADD_CONTACT_DIALOG = "AddContactDialog"

    // The base URL of the server
    const val BASE_URL = "http://178.63.9.114:7777/api/"

    // Key for storing the email in shared preferences
    const val REGISTRATION_EMAIL = "registrationEmail"

    // Key for storing the password in shared preferences
    const val REGISTRATION_PASSWORD = "registrationPassword"

    // Key for storing the accessToken in shared preferences
    const val ACCESS_TOKEN = "accessToken"

    // Key for storing the refreshToken in shared preferences
    const val REFRESH_TOKEN = "refreshToken"
}