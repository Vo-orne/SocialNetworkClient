package com.example.myprofile.mycontacts

import java.io.Serializable

data class Contact(
    val id: Long,
    val avatar: String,
    val name: String,
    val career: String
): Serializable
