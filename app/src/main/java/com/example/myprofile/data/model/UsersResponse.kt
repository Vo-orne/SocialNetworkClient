package com.example.myprofile.data.model

data class UsersResponse(
    val status: String = "",
    val code: Int = 0,
    val message: String? = "",
    val data: Data? = null
) {
    data class Data(val users: List<UserData>?)
}