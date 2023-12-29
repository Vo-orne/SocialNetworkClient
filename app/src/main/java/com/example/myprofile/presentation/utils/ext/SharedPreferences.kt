package com.example.myprofile.presentation.utils.ext

import android.content.SharedPreferences
import com.example.myprofile.presentation.utils.Constants

fun SharedPreferences.Editor.saveUserEmail(email: String) {
    putString(Constants.EMAIL_KEY, email)
}

fun SharedPreferences.getUserEmail(): String {
    return getString(Constants.EMAIL_KEY, "").toString()
}

fun SharedPreferences.Editor.saveUserPassword(password: String) {
    putString(Constants.PASSWORD_KEY, password)
}

fun SharedPreferences.getUserPassword(): String {
    return getString(Constants.PASSWORD_KEY, "").toString()
}

fun SharedPreferences.Editor.saveAutoLoginData(email: String?, password: String?) {
    with(this) {
        email?.let { saveUserEmail(it) }
        password?.let { saveUserPassword(it) }
        apply()
    }
}
