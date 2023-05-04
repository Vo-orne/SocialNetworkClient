package com.example.myprofile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val registerButton: AppCompatButton = findViewById(R.id.viewMyContacts)

//        val isItFromStart = intent.getBooleanExtra("isItFromStart", false)
//        val newUserName = intent.getStringExtra("userName")
//
//        if(isItFromStart) {
//            findViewById<TextView>(R.id.userName).apply {
//                text = newUserName
//            }
//        }

        registerButton.setOnClickListener {
            Intent(this, AuthActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}