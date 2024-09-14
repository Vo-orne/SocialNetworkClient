package com.example.myprofile.presentation.ui.fragments.search.adapter.interfaces

import com.example.myprofile.data.model.Contact

interface SearchActionListener {
    // Clicking on a contact.
    fun onClick(contact: Contact, position: Int)
}