package com.example.myprofile.presentation.ui.fragments.contacts.adapter.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.myprofile.data.model.Contact

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