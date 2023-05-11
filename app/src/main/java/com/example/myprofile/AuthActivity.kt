package com.example.myprofile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText

class AuthActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        sharedPreferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        accountLogin()
    }

    private fun accountLogin() {
        val email = sharedPreferences.getString("email", null)
        if (email == null) {
            defaultAccountLogin()
        } else {
            autoLogin(email)
        }
    }

    private fun defaultAccountLogin() {
        val registerButton: AppCompatButton = findViewById(R.id.buttonRegister)
        val receivedUserEmail = findViewById<TextInputEditText>(R.id.textInputEditTextEmail)
        val receivedUserPassword = findViewById<TextInputEditText>(R.id.textInputEditTextPassword)
        val memberInputDate = findViewById<CheckBox>(R.id.memberInputDate)

        registerButton.setOnClickListener {
            Intent(this, MainActivity::class.java).also {
                val newEmail = receivedUserEmail.text.toString()
                val newPassword = receivedUserPassword.text.toString()
                if (memberInputDate.isChecked) {
                    editor.clear()
                    editor.apply()
                    saveLoginData(newEmail, newPassword)
                }
                comeToNextActivity(newEmail, it)
            }
        }
    }

    private fun saveLoginData(email: String, password: String) {
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

    private fun comeToNextActivity(email: String, it: Intent) {
        val userName: String = parsEmail(email)
        it.putExtra("userName", userName)
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
                email[i] == '@' -> return result
                email[i] == '.' -> {
                    result += " "
                    continue
                }
            }
            result += email[i].lowercaseChar()
        }
        return result
    }
}