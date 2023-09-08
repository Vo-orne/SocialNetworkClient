package com.example.myprofile.ui.adapters

import com.example.myprofile.data.Contact

/**
 * Interface for the listener of contact actions in the adapter.
 */
interface ContactActionListener {
    fun onContactDelete(contact: Contact, position: Int)

    fun onClick(contact: Contact, position: Int)

    fun onLongClick(contact: Contact, position: Int)
}