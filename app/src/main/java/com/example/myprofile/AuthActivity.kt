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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val registerButton: AppCompatButton = findViewById(R.id.buttonRegister)

        sharedPreferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        val receivedUserEmail = findViewById<TextInputEditText>(R.id.textInputEditTextEmail)
        val receivedUserPassword = findViewById<TextInputEditText>(R.id.textInputEditTextPassword)

        val email = getLoginData().first
        val password = getLoginData().second

        receivedUserEmail.setText(email)
        receivedUserPassword.setText(password)

        val memberInputDate = findViewById<CheckBox>(R.id.memberInputDate)

        registerButton.setOnClickListener {
            Intent(this, MainActivity::class.java).also {
                val newEmail: String = receivedUserEmail.text.toString()
                val newPassword = receivedUserPassword.text.toString()
                saveLoginData(newEmail, newPassword)

                val userName: String = parsEmail(receivedUserEmail)
                it.putExtra("userName", userName)
                startActivity(it)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }
        }
    }

    private fun getLoginData(): Pair<String?, String?> {
        val username = sharedPreferences.getString("username", null)
        val password = sharedPreferences.getString("password", null)
        return Pair(username, password)
    }

    private fun saveLoginData(username: String, password: String) {
        with(sharedPreferences.edit()) {
            putString("username", username)
            putString("password", password)
            apply()
        }
    }

    fun saveAutoLoginState(autoLogin: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean("autoLogin", autoLogin)
            apply()
        }
    }

    private fun parsEmail(receivedUserEmail: TextInputEditText): String {
        val source: String = receivedUserEmail.text.toString()
        var result = ""

        for (i in source.indices) {
            if (i == 0 || result[result.length - 1] == ' ') {
                result += source[i].uppercaseChar()
                continue
            }
            when {
                source[i] == '@' -> return result
                source[i] == '.' -> {
                    result += " "
                    continue
                }
            }
            result += source[i].lowercaseChar()
        }
        return result
    }
}