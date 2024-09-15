package com.example.myprofile.presentation.utils.ext

import android.util.Log

/**
 * Extension function for logging debug messages.
 * @param str The message or object to log. It will be converted to a string if it is not already.
 */
fun log(str: Any) {
    Log.d("log", str.toString()) // Log the message with "log" tag at DEBUG level
}