package com.example.myprofile.ui.fragments

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myprofile.R
import com.example.myprofile.utils.Constants

class SignUpViewModel : ViewModel() {
    private val _registerLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val registerLiveData: MutableLiveData<Boolean> = _registerLiveData

    var emailError: String? = null
    var passwordError: String? = null

    fun saveAutoLogin(email: String, password: String) {
        _registerLiveData.value = validationInputs(email, password)
    }

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
}