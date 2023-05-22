package com.example.myprofile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myprofile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.textViewMainUserName.text = intent.getStringExtra(Constants.USER_NAME_KEY)
    }
}