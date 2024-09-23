package com.example.myprofile.presentation.utils.ext

import android.content.SharedPreferences
import com.example.myprofile.presentation.utils.Constants

/**
 * Extension function to save user email in SharedPreferences.
 * @param email The email address to be saved.
 */
fun SharedPreferences.Editor.saveUserEmail(email: String) {
    putString(Constants.EMAIL_KEY, email)
}

/**
 * Extension function to retrieve the user email from SharedPreferences.
 * @return The saved email address, or an empty string if not found.
 */
fun SharedPreferences.getUserEmail(): String {
    return getString(Constants.EMAIL_KEY, "").toString()
}

/**
 * Extension function to save user password in SharedPreferences.
 * @param password The password to be saved.
 */
fun SharedPreferences.Editor.saveUserPassword(password: String) {
    putString(Constants.PASSWORD_KEY, password)
}

/**
 * Extension function to retrieve the user password from SharedPreferences.
 * @return The saved password, or an empty string if not found.
 */
fun SharedPreferences.getUserPassword(): String {
    return getString(Constants.PASSWORD_KEY, "").toString()
}

/**
 * Extension function to save auto-login data (email and password) in SharedPreferences.
 * @param email The email address to be saved.
 * @param password The password to be saved.
 */
fun SharedPreferences.Editor.saveAutoLoginData(email: String?, password: String?) {
    with(this) {
        email?.let { saveUserEmail(it) }
        password?.let { saveUserPassword(it) }
        apply()
    }
}

/**
 * Extension function to remove user email from SharedPreferences.
 */
fun SharedPreferences.Editor.removeUserEmail() {
    remove(Constants.EMAIL_KEY)
}

/**
 * Extension function to remove user password from SharedPreferences.
 */
fun SharedPreferences.Editor.removeUserPassword() {
    remove(Constants.PASSWORD_KEY)
}

/**
 * Extension function to remove auto-login data (email and password) from SharedPreferences.
 */
fun SharedPreferences.Editor.removeAutoLoginData() {
    removeUserEmail()
    removeUserPassword()
    apply()
}
