package com.example.myprofile.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.myprofile.data.Contact

/**
 * The `UsersDiffCallback` class is used to calculate changes between two lists of contacts.
 */
class UsersDiffCallback(
) : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem == newItem

}