package com.example.myprofile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val registerButton: AppCompatButton = findViewById(R.id.buttonRegister)
        val receivedUserEmail: TextInputLayout = findViewById(R.id.editTextEmailAddress)
        val x = findViewById<TextInputEditText>(R.id.textInputEditTextAddress)

        registerButton.setOnClickListener {
            Intent(this, MainActivity::class.java).also {
                val userName: String = parsEmail(receivedUserEmail, x)
                it.putExtra("userName", userName)
                startActivity(it)
                finish()
            }
        }
    }

    private fun parsEmail(receivedUserEmail: TextInputLayout, x: TextInputEditText): String {
        val source: String = x.text.toString()
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