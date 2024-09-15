package com.example.myprofile.presentation.ui.fragments.search.adapter.interfaces

import com.example.myprofile.data.model.Contact

/**
 * Interface for handling actions in the search adapter.
 */
interface SearchActionListener {

    /**
     * Called when a contact is clicked.
     *
     * @param contact The contact that was clicked.
     * @param position The position of the contact in the list.
     */
    fun onClick(contact: Contact, position: Int)
}
