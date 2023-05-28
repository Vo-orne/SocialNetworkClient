package com.example.myprofile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myprofile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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