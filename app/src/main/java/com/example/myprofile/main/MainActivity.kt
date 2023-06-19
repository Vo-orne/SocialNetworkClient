package com.example.myprofile.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myprofile.Constants
import com.example.myprofile.mycontacts.MyContactsActivity
import com.example.myprofile.R
import com.example.myprofile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {  ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.textViewMainUserName.text = intent.getStringExtra(Constants.USER_NAME_KEY)
        setListeners()
    }

    private fun setListeners() {
        binding.buttonMainViewMyContacts.setOnClickListener {
            Intent(this, MyContactsActivity::class.java).also {
                startActivity(it)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }
        }
    }
}