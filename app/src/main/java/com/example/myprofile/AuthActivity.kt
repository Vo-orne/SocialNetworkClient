package com.example.myprofile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myprofile.databinding.ActivitySignUpBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences =
            getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        accountLogin()
    }

    private fun accountLogin() {
        val email = sharedPreferences.getString(Constants.EMAIL_KEY, null)
        if (email == null) {
            defaultAccountLogin()
        } else {
            autoLogin(email)
        }
    }

    private fun defaultAccountLogin() {
        binding.buttonSignUpRegister.setOnClickListener {
            val newEmail = binding.textInputEditTextSignUpEmail.text.toString()
            val newPassword = binding.textInputEditTextSignUpPassword.text.toString()

            if (validateInputs(newEmail, newPassword)) {
                Intent(this, MainActivity::class.java).also {
                    if (binding.checkBoxSignUpMemberInputDate.isChecked) {
                        editor.clear()
                        editor.apply()
                        saveLoginData(newEmail, newPassword)
                    }
                    comeToNextActivity(newEmail, it)
                }
            }
        }
    }

    private fun validateInputs(emailInput: String, passwordInput: String): Boolean {
        var isValid = true

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            isValid = false
            binding.textInputLayoutSignUpEmail.error = Constants.INCORRECTLY_EMAIL_MESSAGE
        } else {
            binding.textInputLayoutSignUpEmail.error = null
        }

        if (passwordInput.isEmpty() || !isValidPassword(passwordInput)) {
            isValid = false
            binding.textInputLayoutSignUpPassword.error = Constants.INCORRECTLY_PASSWORD_MESSAGE
        } else {
            binding.textInputLayoutSignUpPassword.error = null
        }

        return isValid
    }

    private fun isValidPassword(password: String): Boolean {
        return password.all {
            it in Constants.PASSWORD_VERIF_SYM[0]..Constants.PASSWORD_VERIF_SYM[1]
                    || it in Constants.PASSWORD_VERIF_SYM[2]..Constants.PASSWORD_VERIF_SYM[3]
                    && it != Constants.PASSWORD_VERIF_SYM[4]
                    && password.length >= Constants.MAX_PASSWORD_SIZE
        }
    }

    private fun saveLoginData(email: String, password: String) {
        editor.putString(Constants.EMAIL_KEY, email)
        editor.putString(Constants.PASSWORD_KEY, password)
        editor.apply()
    }

    private fun comeToNextActivity(email: String, it: Intent) {
        val userName: String = parsEmail(email)
        it.putExtra(Constants.USER_NAME_KEY, userName)
        startActivity(it)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    private fun autoLogin(email: String) {
        Intent(this, MainActivity::class.java).also {
            comeToNextActivity(email, it)
        }
    }

    private fun parsEmail(email: String): String {
        var result = ""

        for (i in email.indices) {
            if (i == 0 || result[result.length - 1] == ' ') {
                result += email[i].uppercaseChar()
                continue
            }
            when {
                email[i] == Constants.EMAIL_PARSING_SYMBOLS[0] -> return result
                email[i] == Constants.EMAIL_PARSING_SYMBOLS[1] -> {
                    result += " "
                    continue
                }
            }
            result += email[i].lowercaseChar()
        }
        return result
    }
}