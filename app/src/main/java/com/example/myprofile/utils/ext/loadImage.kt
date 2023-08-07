package com.example.myprofile.utils.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myprofile.R

fun ImageView.loadImage(image: String?) {
    Glide.with(this)
        .load(image)
        .circleCrop()
        .placeholder(R.drawable.default_user_photo)
        .error(R.drawable.default_user_photo)
        .into(this)
}