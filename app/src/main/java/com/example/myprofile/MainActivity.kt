package com.example.myprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myprofile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val newUserName = intent.getStringExtra(Constants.USER_NAME_KEY)
        binding.userName.apply {
            text = newUserName
        }
    }
}