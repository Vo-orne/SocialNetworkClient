package com.example.myprofile.presentation.utils

/**
 * The `Constants` object contains constants used throughout the application.
 * All variables in this object are immutable and used for various purposes within the app.
 */
object Constants {

    // Key for SharedPreferences used for storing login data.
    const val SHARED_PREFERENCES_KEY = "login_data"

    // Keys for storing email and password in SharedPreferences.
    const val EMAIL_KEY = "email"
    const val PASSWORD_KEY = "password"

    // Minimum length required for passwords.
    const val MIN_PASSWORD_SIZE = 8

    // Base URL for the server API.
    const val BASE_URL = "http://178.63.9.114:7777/api/"

    // Prefix used for authorization tokens.
    const val AUTHORIZATION_PREFIX = "Bearer"

    // Identifier for the dialog used to edit user profiles.
    const val EDIT_USER_DIALOG = "EditProfileDialog"

    // Date formats used in the application.
    const val DATE_FORMAT = "dd/MM/yyyy"

    // Channel details for push notifications.
    const val CHANNEL_ID = "0"
    const val CHANNEL_NAME = "Channel name"

    // Log messages used for debugging network and database operations.
    const val INTERNET = "Internet is available, fetching contacts from server"
    const val NO_INTERNET = "No internet connection, fetching contacts from local database"
    const val BD_EMPTY = "No internet connection and local contacts database is empty"

    // Prefix for API error messages from UserRepositoryImpl.
    const val API_ERROR = "ApiState.Error = "

    // Constants used for defining database names in DatabaseModule.
    const val CONTACTS_DB = "contacts_database"
    const val USERS_DB = "users_database"
}
