package com.example.myprofile.ui.mycontacts

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.databinding.ContactItemBinding
import com.bumptech.glide.Glide
import com.example.myprofile.R
import com.example.myprofile.ui.utils.etentions.setImageWithGlide
import com.google.android.material.snackbar.Snackbar

class ContactsAdapter(
    var contacts: MutableList<Contact>,
    private val applicationContext: ConstraintLayout,       //todo delete
    private val context: Context                            //todo delete
) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {     //todo  ListAdapter adapter with diff-util


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ContactItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactViewHolder(binding)
    }

        override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)    //todo simplify
    }



    inner class ContactViewHolder(private val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(contact: Contact) {
            binding.textViewMyContactsUserName.text = contact.name
            binding.textViewMyContactsUserCareer.text = contact.career

            binding.imageViewMyContactsUserAvatar.setImageWithGlide(contact.avatar)

//            Glide.with(binding.root)        //todo extract to extension
//                .load(contact.avatar)
//                .circleCrop()                                   // Performs image processing in the form of a circle
//                .placeholder(R.drawable.default_user_photo)     // A placeholder image that is displayed until the image is loaded
//                .error(R.drawable.default_user_photo)           // The image that is displayed in case of a download error
//                .into(binding.imageViewMyContactsUserAvatar)

            binding.buttonMyContactsDelete.setOnClickListener {
                val position = bindingAdapterPosition       //todo better do it in viewModel or repository (google architecture)
                if (position != RecyclerView.NO_POSITION) {
                    deleteContact(position)
                }
                showUndoSnackbar(contact, position)
            }
        }
    }

    fun showUndoSnackbar(contact: Contact, position: Int) {         //todo it is not a part of recycler
        val snackbar = Snackbar.make(
            applicationContext,         //todo not good
            context.getString(R.string.contact_removed),
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(context.getString(R.string.cancel)) {
            undoDeleteContact(contact, position)
        }
        snackbar.show()

        // Automatically close the Snackbar after 5 seconds
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable { snackbar.dismiss() }
        handler.postDelayed(runnable, 5000)     //todo delay -> constants
    }

    private fun undoDeleteContact(contact: Contact, position: Int) {    //todo all logic to viewModel
        contacts.add(position, contact)
        notifyItemInserted(position)
    }



    override fun getItemCount(): Int {
        return contacts.size
    }

    fun deleteContact(position: Int) {      //todo all logic to viewModel
        contacts.removeAt(position)
        notifyItemRemoved(position)
    }

    // Getting a contact by position
    fun getContactAtPosition(position: Int): Contact {
        return contacts[position]
    }
}