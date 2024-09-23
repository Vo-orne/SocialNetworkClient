package com.example.myprofile.presentation.ui.fragments.contacts.adapter.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.myprofile.data.model.Contact

/**
 * Used to calculate the differences between two lists of contacts.
 * Used by RecyclerView's adapter to efficiently update the list when data changes.
 */
class UsersDiffCallback : DiffUtil.ItemCallback<Contact>() {

    /**
     * Checks if two items represent the same contact.
     * @param oldItem The contact in the old list.
     * @param newItem The contact in the new list.
     * @return True if the contacts are the same, otherwise false.
     */
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem.id == newItem.id

    /**
     * Checks if the contents of two contacts are the same.
     * @param oldItem The contact in the old list.
     * @param newItem The contact in the new list.
     * @return True if the contents of the contacts are the same, otherwise false.
     */
    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem == newItem
}