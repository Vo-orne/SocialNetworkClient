package com.example.myprofile.presentation.ui.fragments.contacts.adapter.interfaces

import com.example.myprofile.data.model.Contact

/**
 * Interface for handling actions related to contacts in the adapter.
 */
interface ContactActionListener {

    /**
     * Method called when a contact's delete button is clicked.
     * @param contact The contact to be deleted.
     * @param position The position of the contact in the list.
     */
    fun onContactDelete(contact: Contact, position: Int)

    /**
     * Method called when a contact is clicked.
     * @param contact The contact that was clicked.
     * @param position The position of the contact in the list.
     */
    fun onClick(contact: Contact, position: Int)

    /**
     * Method called when a contact is long-pressed.
     * @param contact The contact that was long-pressed.
     * @param position The position of the contact in the list.
     */
    fun onLongClick(contact: Contact, position: Int)
}
