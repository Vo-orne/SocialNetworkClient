package com.example.myprofile.utils.ext

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myprofile.R

fun ImageView.loadImage(image: String? = null) {
    Glide.with(this)
        .load(if(image.isNullOrBlank()) R.drawable.default_user_photo else image)
        .circleCrop()
        .placeholder(R.drawable.default_user_photo)
        .error(R.drawable.default_user_photo)
        .into(this)
}

fun ImageView.loadImage(imageUri: Uri) {
    Glide.with(this)
        .load(imageUri)
        .into(this)
}