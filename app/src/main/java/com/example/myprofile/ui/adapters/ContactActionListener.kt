package com.example.myprofile.ui.adapters

import com.example.myprofile.data.Contact

/**
 * Interface for the listener of contact actions in the adapter.
 */
interface ContactActionListener {
    // Clicking on the button to delete a contact.
    fun onContactDelete(contact: Contact, position: Int)

    // Clicking on a contact.
    fun onClick(contact: Contact, position: Int)

    // Long press on the contact.
    fun onLongClick(contact: Contact, position: Int)
}