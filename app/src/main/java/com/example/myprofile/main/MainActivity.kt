package com.example.myprofile.main

import android.content.Context
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

        val sharedPreferences = getSharedPreferences(
            Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE
        )
        val userName = sharedPreferences.getString(Constants.USER_NAME_KEY, "")
        binding.textViewMainUserName.text = userName
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