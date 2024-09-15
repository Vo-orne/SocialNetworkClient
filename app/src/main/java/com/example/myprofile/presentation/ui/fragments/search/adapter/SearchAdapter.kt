package com.example.myprofile.presentation.ui.fragments.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.ContactItemBinding
import com.example.myprofile.presentation.ui.fragments.contacts.adapter.utils.UsersDiffCallback
import com.example.myprofile.presentation.ui.fragments.search.adapter.interfaces.SearchActionListener
import com.example.myprofile.presentation.utils.ext.gone
import com.example.myprofile.presentation.utils.ext.loadImage

/**
 * Adapter for displaying search results in a RecyclerView.
 *
 * @param listener The listener to handle click events on the contact items.
 */
class SearchAdapter(private val listener: SearchActionListener) :
    ListAdapter<Contact, SearchAdapter.UsersViewHolder>(UsersDiffCallback()) {

    /**
     * Creates a new [UsersViewHolder] when there are no existing view holders.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ContactItemBinding.inflate(inflater, parent, false)
        return UsersViewHolder(binding)
    }

    /**
     * Binds the data to the [UsersViewHolder].
     */
    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    /**
     * Inner class that represents an individual contact element.
     */
    inner class UsersViewHolder(private val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the contact data to the view.
         */
        fun bind(contact: Contact) {
            with(binding) {
                textViewContactItemUserName.text = contact.name
                textViewContactItemUserCareer.text = contact.career
                imageViewContactItemUserAvatar.loadImage(contact.avatar)
                buttonContactItemDelete.gone() // Hide the delete button
            }
            setListeners(contact)
        }

        /**
         * Sets click listeners for the contact item.
         */
        private fun setListeners(contact: Contact) {
            binding.root.setOnClickListener {
                listener.onClick(contact, bindingAdapterPosition)
            }
        }
    }
}
