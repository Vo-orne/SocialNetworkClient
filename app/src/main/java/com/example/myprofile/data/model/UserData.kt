package com.example.myprofile.data.model

import java.util.Date

data class UserData(
    val image: String? = null,
    val name: String? = null,
    val career: String? = null,
    val address: String? = null,
    val id: Long,
    val email: String,
    val phone: String? = null,
    val birthday: Date? = null,
    val facebook: String? = null,
    val instagram: String? = null,
    val twitter: String? = null,
    val linkedin: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)  {
    fun toContact(): Contact = Contact(image, name, career, address, id)
}