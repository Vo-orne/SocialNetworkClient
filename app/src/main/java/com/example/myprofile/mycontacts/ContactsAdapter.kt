package com.example.myprofile.mycontacts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.databinding.ActivityContactItemBinding
import com.bumptech.glide.Glide
import com.example.myprofile.R
import com.google.android.material.snackbar.Snackbar

class ContactsAdapter(
    var contacts: MutableList<Contact>,
    private val applicationContext: ConstraintLayout,
    private val context: Context
) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(private val binding: ActivityContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.textViewMyContactsUserName.text = contact.name
            binding.textViewMyContactsUserCareer.text = contact.career

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
                showUndoSnackbar(contact, position)
            }
        }
    }

    private fun showUndoSnackbar(contact: Contact, position: Int) {
        val snackbar = Snackbar.make(
            applicationContext,
            context.getString(R.string.contact_removed),
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(context.getString(R.string.cancel)) {
            undoDeleteContact(contact, position)
        }
        snackbar.show()

        // Automatically close the Snackbar after 5 seconds
        val handler = android.os.Handler()
        val runnable = Runnable { snackbar.dismiss() }
        handler.postDelayed(runnable, 5000)
    }

    private fun undoDeleteContact(contact: Contact, position: Int) {
        contacts.add(position, contact)
        notifyItemInserted(position)
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

    private fun deleteContact(position: Int) {
        contacts.removeAt(position)
        notifyItemRemoved(position)
    }
}