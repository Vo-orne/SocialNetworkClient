package com.example.myprofile.presentation.utils.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myprofile.R

/**
 * Extension function to load an image into an ImageView using Glide.
 * @param image The URL or path of the image to load. If null or blank, a default image is used.
 */
fun ImageView.loadImage(image: String? = null) {
    Glide.with(this) // Initialize Glide with the current context (ImageView's context)
        .load(
            // If the image URL is null or blank, load the default user photo
            if (image.isNullOrBlank()) R.drawable.default_user_photo else image
        )
        .circleCrop() // Apply a circular crop to the image
        .placeholder(R.drawable.default_user_photo) // Set a placeholder image while loading
        .error(R.drawable.default_user_photo) // Set an image to display in case of an error
        .into(this) // Load the image into the current ImageView
}