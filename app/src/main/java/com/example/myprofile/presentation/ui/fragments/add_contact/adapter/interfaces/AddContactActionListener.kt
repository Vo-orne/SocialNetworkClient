package com.example.myprofile.presentation.ui.fragments.add_contact.adapter.interfaces

import com.example.myprofile.data.model.Contact

interface AddContactActionListener {

    // Clicking on a contact.
    fun onClickAddButton(contact: Contact, position: Int)
//    fun onClickContact(contact: Contact, transitionPairs: Array<Pair<View, String>>)
}