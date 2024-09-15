package com.example.myprofile.presentation.ui.fragments.add_contact.adapter.interfaces

import com.example.myprofile.data.model.Contact

/**
 * Interface to handle actions related to adding a contact in the AddContactFragment.
 */
interface AddContactActionListener {

    /**
     * Called when the add button for a contact is clicked.
     *
     * @param contact The contact item associated with the add button click.
     * @param position The position of the contact item in the list.
     */
    fun onClickAddButton(contact: Contact, position: Int)
}
