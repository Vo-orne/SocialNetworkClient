package com.example.myprofile.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.myprofile.constants.Constants
import com.example.myprofile.ui.main.MainActivity
import com.example.myprofile.R
import com.example.myprofile.databinding.ActivitySignUpBinding
import com.example.myprofile.ui.utils.etentions.navigateToActivity
import java.util.*


class AuthActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }       //todo can be move to base-activity class
    private val sharedPreferences by lazy {
        getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        accountAutoLogin()
        setListeners()
    }

    private fun accountAutoLogin() {
        val email = sharedPreferences.getString(Constants.EMAIL_KEY, "") ?: ""
        if (email.isNotEmpty()) {
            autoLogin()
        }
    }

    private fun autoLogin() {
        navigateToActivity(MainActivity::class.java)
//        Intent(this, MainActivity::class.java).also {
//            comeToNextActivity(it)
//        }
    }

    private fun comeToNextActivity(it: Intent) {
        startActivity(it)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }


    private fun parsEmail(email: String): String {      //todo maybe do it in main?
        val substring = email.substring(0, email.indexOf('@')) // qwe.rty@d.asd -> qwe.rty
        val splittedEmail = substring.split('.') // qwe.rty -> [qwe, rty]

        if (splittedEmail.size == 1) {
            return splittedEmail[0]
        }
        val sb = StringBuilder()
        splittedEmail.forEach {
            val word = it.capitalize(Locale.ROOT)       //todo Deprecated
            sb.append("$word ")
        }
        return sb.substring(0, sb.length - 1).toString()    //todo why -1?
    }

    private fun setListeners() {
        with(binding) {
            buttonSignUpRegister.setOnClickListener {
                val newEmail = textInputEditTextSignUpEmail.text.toString()
                val newPassword = textInputEditTextSignUpPassword.text.toString()

                val editor =
                    sharedPreferences.edit()   //todo whats happiness when inputs not valid?

                if (validateInputs(newEmail, newPassword)) {
                    if (checkBoxSignUpMemberInputDate.isChecked) {
                        editor.clear()
                        editor.apply()
                        saveLoginData(newEmail, newPassword, editor)
                    }
                    saveUserName(newEmail, editor)
                    navigateToActivity(MainActivity::class.java)
//                    Intent(this@AuthActivity, MainActivity::class.java).also {   //todo DRY (39)
//                        comeToNextActivity(it)
//                    }
                }
            }
        }
    }

    private fun validateInputs(emailInput: String, passwordInput: String): Boolean {
        var isValid = true      //todo return

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {    //todo decompose
            isValid = false
            binding.textInputLayoutSignUpEmail.error = getString(R.string.error_on_email)
        } else {
            binding.textInputLayoutSignUpEmail.error = null
        }

        if (isValidPassword(passwordInput) == Constants.PASSWORD_IS_CORRECT) {  //todo decompose
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

        if (password.contains(Regex("\\d+"))) isHasNumber = true        //todo rejects to const
        if (password.contains(Regex("[a-zA-Z]+"))) isHasLetter = true   //todo rejects to const

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

    private fun saveUserName(email: String, editor: SharedPreferences.Editor) {
        val userName: String = parsEmail(email)
        editor.putString(Constants.USER_NAME_KEY, userName)
        editor.apply()
    }
}