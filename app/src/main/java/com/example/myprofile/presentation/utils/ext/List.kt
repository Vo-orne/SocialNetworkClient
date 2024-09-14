package com.example.myprofile.presentation.utils.ext

import com.example.myprofile.data.model.Contact

fun List<Contact>.filterContacts(query: String): List<Contact> {
    return this.filter { contact ->
        contact.name?.contains(query, ignoreCase = true) ?: false
    }
}