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

class SearchAdapter(private val listener: SearchActionListener) :
    ListAdapter<Contact, SearchAdapter.UsersViewHolder>(UsersDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchAdapter.UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ContactItemBinding.inflate(inflater, parent, false)
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class UsersViewHolder(private val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            with(binding) {
                textViewContactItemUserName.text = contact.name
                textViewContactItemUserCareer.text = contact.career
                imageViewContactItemUserAvatar.loadImage(contact.avatar)
                buttonContactItemDelete.gone()
            }
        }

        fun setListeners(contact: Contact) {
            binding.root.setOnClickListener {
                listener.onClick(contact, bindingAdapterPosition)
            }
        }
    }
}