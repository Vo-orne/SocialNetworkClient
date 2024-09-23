package com.example.myprofile.presentation.utils.ext

import com.example.myprofile.data.model.Contact

/**
 * Filters a list of contacts based on a search query.
 * @param query The search query used to filter the contacts.
 * @return A list of contacts that match the search query.
 */
fun List<Contact>.filterContacts(query: String): List<Contact> {
    return this.filter { contact ->
        // Check if the contact's name contains the query, ignoring case.
        contact.name?.contains(query, ignoreCase = true) ?: false
    }
}