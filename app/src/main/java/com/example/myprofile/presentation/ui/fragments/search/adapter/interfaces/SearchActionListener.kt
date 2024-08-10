package com.example.myprofile.presentation.ui.fragments.search.adapter.interfaces

import com.example.myprofile.data.database.Contact

interface SearchActionListener {
    // Clicking on a contact.
    fun onClick(contact: Contact, position: Int)
}