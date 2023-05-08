package com.example.myprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newUserName = intent.getStringExtra("userName")
        findViewById<TextView>(R.id.userName).apply {
            text = newUserName
        }
    }
}