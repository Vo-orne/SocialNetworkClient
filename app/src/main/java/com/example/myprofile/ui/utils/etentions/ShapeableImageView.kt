package com.example.myprofile.ui.utils.etentions

import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myprofile.R
import com.google.android.material.imageview.ShapeableImageView

val GLIDE_OPTIONS = RequestOptions()
    .centerCrop()
    .circleCrop()   // Performs image processing in the form of a circle
    .placeholder(R.drawable.default_user_photo) // A placeholder image that is displayed until the image is loaded
    .error(R.drawable.default_user_photo) // The image that is displayed in case of a download error
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .priority(Priority.HIGH)

fun ShapeableImageView.setImageWithGlide(avatar: String) {
    Glide.with(context)        //todo extract to extension
        .load(avatar)
        .apply(GLIDE_OPTIONS)
        .into(this)
}
