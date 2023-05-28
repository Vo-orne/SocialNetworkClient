package com.example.myprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.databinding.ActivityMyContactsBinding

class ContactsAdapter(val contacts: MutableList<Contact>) :
    RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(private val binding: ActivityMyContactsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.textViewMyContactsUserName.text = contact.name
            binding.textViewMyContactsUserCareer.text = contact.career

            binding.buttonMyContactsDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    deleteContact(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ActivityMyContactsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    fun deleteContact(position: Int) {
        contacts.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getMutableContacts(): MutableList<Contact> {
        return contacts
    }
}


