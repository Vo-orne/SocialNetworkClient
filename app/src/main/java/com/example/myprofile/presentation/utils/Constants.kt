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

    // The base URL of the server
    const val BASE_URL = "http://178.63.9.114:7777/api/"

    // Authorization prefix.
    const val AUTHORIZATION_PREFIX = "Bearer"

    // String command to open the add contact dialog.
    const val EDIT_USER_DIALOG = "EditProfileDialog"

    // The dates formats
    const val DATE_FORMAT = "dd/MM/yyyy"
    const val INPUT_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy"

    // Data for creating push notifications
    const val CHANNEL_ID = "0"
    const val CHANNEL_NAME = "Channel name"

    // Logs
    const val INTERNET = "Internet is available, fetching contacts from server"
    const val NO_INTERNET = "No internet connection, fetching contacts from local database"
    const val BD_EMPTY = "No internet connection and local contacts database is empty"
}