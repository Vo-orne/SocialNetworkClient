package com.example.myprofile.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.myprofile.Constants
import com.example.myprofile.main.MainActivity
import com.example.myprofile.R
import com.example.myprofile.databinding.ActivitySignUpBinding
import java.util.*


class AuthActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
    private val sharedPreferences by lazy {
        getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        accountAutoLogin(sharedPreferences)
        setListeners(sharedPreferences)
    }

    private fun accountAutoLogin(sharedPreferences: SharedPreferences) {
        val email = sharedPreferences.getString(Constants.EMAIL_KEY, "") ?: ""
        if (email.isNotEmpty()) {
            autoLogin(email)
        }
    }

    private fun autoLogin(email: String) {
        Intent(this, MainActivity::class.java).also {
            comeToNextActivity(email, it)
        }
    }

    private fun comeToNextActivity(email: String, it: Intent) {
        val userName: String = parsEmail(email)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.USER_NAME_KEY, userName)
        editor.apply()
        startActivity(it)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    private fun parsEmail(email: String): String {
        val substring = email.substring(0, email.indexOf('@')) // qwe.rty@d.asd -> qwe.rty
        val splittedEmail = substring.split('.') // qwe.rty -> [qwe, rty]

        if (splittedEmail.size == 1) {
            return splittedEmail[0]
        }
        val sb = StringBuilder()
        splittedEmail.forEach {
            val word = it.capitalize(Locale.ROOT)
            sb.append("$word ")
        }
        return sb.substring(0, sb.length - 1).toString()
    }

    private fun setListeners(sharedPreferences: SharedPreferences) {
        binding.buttonSignUpRegister.setOnClickListener {
            val newEmail = binding.textInputEditTextSignUpEmail.text.toString()
            val newPassword = binding.textInputEditTextSignUpPassword.text.toString()

            if (validateInputs(newEmail, newPassword)) {
                if (binding.checkBoxSignUpMemberInputDate.isChecked) {
                    val editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()
                    saveLoginData(newEmail, newPassword, editor)
                }
                Intent(this, MainActivity::class.java).also {
                    comeToNextActivity(newEmail, it)
                }
            }
        }
    }

    private fun validateInputs(emailInput: String, passwordInput: String): Boolean {
        var isValid = true

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            isValid = false
            binding.textInputLayoutSignUpEmail.error = getString(R.string.error_on_email)
        } else {
            binding.textInputLayoutSignUpEmail.error = null
        }

        if (isValidPassword(passwordInput) == Constants.PASSWORD_IS_CORRECT) {
            binding.textInputLayoutSignUpPassword.error = null
        } else {
            isValid = false
            binding.textInputLayoutSignUpPassword.error = isValidPassword(passwordInput)
        }

        return isValid
    }

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
            !isHasEnoughLength -> getString(R.string.error_password_is_short)
            !isHasNumber -> getString(R.string.error_password_without_numbers)
            !isHasLetter -> getString(R.string.error_password_without_letters)
            else -> Constants.PASSWORD_IS_CORRECT
        }
    }

    private fun saveLoginData(email: String, password: String, editor: SharedPreferences.Editor) {
        editor.putString(Constants.EMAIL_KEY, email)
        editor.putString(Constants.PASSWORD_KEY, password)
        editor.apply()
    }
}