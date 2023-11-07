package com.example.myprofile.data.model

data class UserResponse(
    val user: Contact,
    val accessToken: String,
    val refreshToken: String
)
