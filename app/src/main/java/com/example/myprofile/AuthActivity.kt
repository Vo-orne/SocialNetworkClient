package com.example.myprofile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputLayout

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val registerButton: AppCompatButton = findViewById(R.id.buttonRegister)
//        val receivedUserEmail: TextInputLayout = findViewById(R.id.editTextEmailAddress)
//
//        val userName: String = parsEmail(receivedUserEmail)

        registerButton.setOnClickListener {
            Intent(this, MainActivity::class.java).also {
//                it.putExtra("isItFromStart", true)
//                it.putExtra("userName", userName)
                startActivity(it)
                finish()
            }
        }
    }

    private fun parsEmail(receivedUserEmail: TextInputLayout): String {
        val source: String = receivedUserEmail.toString()
        var result = ""

        for(i in source.indices) {
            if (source[i] == '@') {
                return result
            } else if (source[i] == '.') {
                result += " "
                continue
            }
            result += source[i]
        }
        return ""
    }
}