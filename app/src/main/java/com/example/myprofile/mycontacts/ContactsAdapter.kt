package com.example.myprofile.mycontacts

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.databinding.ActivityContactItemBinding
import com.bumptech.glide.Glide
import com.example.myprofile.R

class ContactsAdapter(var contacts: MutableList<Contact>) :
    RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(private val binding: ActivityContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.textViewMyContactsUserName.text = contact.name
            binding.textViewMyContactsUserCareer.text = contact.career

            Log.d("myTag", contact.avatar)

            Glide.with(binding.root)
                .load(contact.avatar)
                .circleCrop() // Performs image processing in the form of a circle
                .placeholder(R.drawable.default_user_photo) // A placeholder image that is displayed until the image is loaded
                .error(R.drawable.default_user_photo) // The image that is displayed in case of a download error
                .into(binding.imageViewMyContactsUserAvatar)

            binding.buttonMyContactsDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    deleteContact(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ActivityContactItemBinding.inflate(
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
}