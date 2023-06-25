package com.example.myprofile.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myprofile.constants.Constants
import com.example.myprofile.ui.mycontacts.MyContactsActivity
import com.example.myprofile.databinding.ActivityMainBinding
import com.example.myprofile.ui.utils.etentions.navigateToActivity

class MainActivity : AppCompatActivity() {
    private val binding by lazy {  ActivityMainBinding.inflate(layoutInflater) }    //todo base-class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(
            Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE
        )
        val userName = sharedPreferences.getString(Constants.USER_NAME_KEY, "") //todo decompose lines 19-23 in method
        binding.textViewMainUserName.text = userName
        setListeners()
    }

    private fun setListeners() {
        binding.buttonMainViewMyContacts.setOnClickListener {
            navigateToActivity(MyContactsActivity::class.java)
//            Intent(this, MyContactsActivity::class.java).also {     //todo maybe decompose?
//                startActivity(it)
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//                finish()
//            }
        }
    }
}