package com.example.myprofile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText

class AuthActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val registerButton: AppCompatButton = findViewById(R.id.buttonRegister)

        sharedPreferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val receivedUserEmail = findViewById<TextInputEditText>(R.id.textInputEditTextEmail)
        val receivedUserPassword = findViewById<TextInputEditText>(R.id.textInputEditTextPassword)
        val memberInputDate = findViewById<CheckBox>(R.id.memberInputDate)

        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("password", null)

        if (email == null) {
            Log.d("myTag", sharedPreferences.contains("email").toString())
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
        } else {
            Intent(this, MainActivity::class.java).also {
                comeToNextActivity(email.toString(), it)
            }
        }
    }

    private fun comeToNextActivity(email: String, it: Intent) {
        val userName: String = parsEmail(email)
        it.putExtra("userName", userName)
        startActivity(it)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    private fun saveLoginData(email: String, password: String) {
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
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