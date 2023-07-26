package com.example.myprofile.data

import java.io.Serializable

data class Contact(
    val id: Long,
    val avatar: String,
    val name: String,
    val career: String,
    val address: String
): Serializable
