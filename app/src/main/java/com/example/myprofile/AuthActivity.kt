package com.example.myprofile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.myprofile.databinding.ActivitySignUpBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
//    private lateinit var sharedPreferences: SharedPreferences
//    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences =
            getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
//        editor = sharedPreferences.edit()

        accountAutoLogin(sharedPreferences)
        setListeners(sharedPreferences)

    }

    private fun accountAutoLogin(sharedPreferences: SharedPreferences) {
        val email = sharedPreferences.getString(Constants.EMAIL_KEY, "") ?: ""
        if (email.isNotEmpty()) {
            autoLogin(email)
        }
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
        var isWithoutSymbols = true

        if (password.length >= Constants.MAX_PASSWORD_SIZE) {
            isHasEnoughLength = true
        }

        for (i in password) {
//            when (i) {
//                in Constants.PASSWORD_VERIF_SYM[0]..Constants.PASSWORD_VERIF_SYM[1]
//                -> isHasNumber = true
//
//                in Constants.PASSWORD_VERIF_SYM[2]..Constants.PASSWORD_VERIF_SYM[3],
//                in Constants.PASSWORD_VERIF_SYM[4]..Constants.PASSWORD_VERIF_SYM[5]
//                -> isHasLetter = true
//
//                else -> isWithoutSymbols = false
//            }

            if (password.contains(Regex("\\d+"))) isHasNumber = true
            if (password.contains(Regex("[a-zA-Z]+"))) isHasLetter = true
            if (!isHasEnoughLength || !isHasLetter) isWithoutSymbols = false

        }

        return when {
            !isHasEnoughLength -> Constants.PASSWORD_IS_SHORT // TODO getString(R.string.)
            !isHasNumber -> Constants.PASSWORD_WITHOUT_NUMBERS
            !isHasLetter -> Constants.PASSWORD_WITHOUT_LETTERS
            !isWithoutSymbols -> Constants.PASSWORD_WITH_CHARACTERS
            else -> Constants.PASSWORD_IS_CORRECT
        }
    }

    private fun saveLoginData(email: String, password: String, editor: SharedPreferences.Editor) {
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
        //das.das@d.asd

        val substring = email.substring(0, email.indexOf('@')) // qwe.rty@d.asd -> qwe.rty
        val splittedEmail = substring.split('.') // qwe.rty -> [qwe, rty]

        return when (splittedEmail.size) {
            1 -> splittedEmail[0]
            else -> {
                val sb = StringBuilder()
                splittedEmail.forEach { sb.append("$it ") }
                sb.substring(0, sb.length - 1).toString()
            }
        }

//        for (i in email.indices) {
//            if (i == 0 || result[result.length - 1] == ' ') {
//                result += email[i].uppercaseChar()
//                continue
//            }
//            when {
//                email[i] == Constants.EMAIL_PARSING_SYMBOLS[0] -> return result
//                email[i] == Constants.EMAIL_PARSING_SYMBOLS[1] -> {
//                    result += " "
//                    continue
//                }
//            }
//            result += email[i].lowercaseChar()
//        }
//        return result
    }
}