package com.example.myprofile

class Constants {
    companion object {
        const val SHARED_PREFERENCES_KEY = "login_data"
        const val EMAIL_KEY = "email"
        const val PASSWORD_KEY = "password"
        const val USER_NAME_KEY = "userName"
        val EMAIL_PARSING_SYMBOLS = arrayOf('@', '.')
        val PASSWORD_VERIF_SYM = arrayOf('0', '9', 'a', 'z', '_')
        const val MAX_PASSWORD_SIZE = 8
        const val INCORRECTLY_EMAIL_MESSAGE = "Enter a valid email."
        const val INCORRECTLY_PASSWORD_MESSAGE = "Enter a valid password."
    }
}
