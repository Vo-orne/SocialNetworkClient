package com.example.myprofile.data.model

import com.example.myprofile.data.database.Contact


data class UserResponse(
    val status: String = "",
    val code: Int = 0,
    val message: String? = "",
    val data: Data? = null
) {
    data class Data(
        val user: Contact,
        val accessToken: String,
        val refreshToken: String
    )
}