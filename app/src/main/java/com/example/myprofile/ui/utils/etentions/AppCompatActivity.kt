package com.example.myprofile.ui.utils.etentions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.myprofile.R

fun <T> AppCompatActivity.navigateToActivity(cls: Class<T>){
    Intent(this, cls).also {
        startActivity(it)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }
}