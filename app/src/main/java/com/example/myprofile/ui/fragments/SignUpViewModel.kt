package com.example.myprofile.ui.fragments

import android.content.SharedPreferences
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myprofile.R
import com.example.myprofile.utils.Constants
import java.util.Locale

class SignUpViewModel : ViewModel() {
    private val _registerLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val registerLiveData: MutableLiveData<Boolean> = _registerLiveData

    var emailError: String? = null
    var passwordError: String? = null

    /**
     * Private method to validate the entered data
     */
    fun validationInputs(email: String, password: String): Boolean {
        var isValid = true

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false
            emailError = R.string.error_on_email.toString()
        }

        // Validate the entered password
        if (isValidPassword(password) != Constants.PASSWORD_IS_CORRECT) {
            isValid = false
            passwordError = isValidPassword(password)
        }

        return isValid
    }

    /**
     * Private method to validate the entered password
     */
    private fun isValidPassword(password: String): String {
        var isHasEnoughLength = false
        var isHasNumber = false
        var isHasLetter = false

        if (password.length >= Constants.MAX_PASSWORD_SIZE) {
            isHasEnoughLength = true
        }

        if (password.contains(Regex("\\d+"))) isHasNumber = true
        if (password.contains(Regex("[a-zA-Z]+"))) isHasLetter = true

        return when {
            !isHasEnoughLength -> R.string.error_password_is_short.toString()
            !isHasNumber -> R.string.error_password_without_numbers.toString()
            !isHasLetter -> R.string.error_password_without_letters.toString()
            else -> Constants.PASSWORD_IS_CORRECT
        }
    }

    fun saveAutoLogin(email: String, password: String, editor: SharedPreferences.Editor) {
        _registerLiveData.value = validationInputs(email, password)
        if(_registerLiveData.value == true) {
            editor.putBoolean(Constants.AUTO_LOGIN_KEY, _registerLiveData.value!!)
            editor.apply()
        }
    }

    /**
     * Method to save the user's name from the entered email
     */
    fun saveUserName(email: String, editor: SharedPreferences.Editor) {
        val userName: String = parsEmail(email)
        editor.putString(Constants.USER_NAME_KEY, userName)
        editor.apply()
    }

    /**
     * Private method to parse the entered email and create the user's name from it
     */
    private fun parsEmail(email: String): String {
        val substring = email.substring(0, email.indexOf('@')) // qwe.rty@d.asd -> qwe.rty
        val splittedEmail = substring.split('.') // qwe.rty -> [qwe, rty]

        if (splittedEmail.size == 1) {
            return splittedEmail[0]
        }
        val sb = StringBuilder()
        splittedEmail.forEach { it ->
            val word =
                it.replaceFirstChar {
                    if (it.isLowerCase())
                        it.titlecase(Locale.ROOT)
                    else
                        it.toString()
                }
            sb.append("$word ")
        }
        return sb.substring(0, sb.length - 1).toString()
    }
}