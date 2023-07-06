package com.example.myprofile.mycontacts

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.databinding.ContactItemBinding
import com.bumptech.glide.Glide
import com.example.myprofile.R


interface ContactActionListener {
    fun onContactDelete(contact: Contact)
    fun onDetailView(contact: Contact)
}

class UsersDiffCallback(
    private val oldList: List<Contact>,
    private val newList: List<Contact>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser.id == newUser.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser == newUser
    }
}

class ContactsAdapter(
    private val actionListener: ContactActionListener
) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>(), View.OnClickListener {

    var contacts: List<Contact> = emptyList()
        set(newValue) {
            val diffCallback = UsersDiffCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onClick(v: View) {
        //Log.d("MyTag", v.tag.toString())
        val contact = v.tag as Contact
        when (v.id) {
            R.id.buttonMyContactsDelete -> {
                actionListener.onContactDelete(contact)
            }
            else -> {
                actionListener.onDetailView(contact)
            }
        }
    }

    override fun getItemCount(): Int = contacts.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ContactItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.buttonMyContactsDelete.setOnClickListener(this)

        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        with(holder.binding) {
            holder.itemView.tag = contact
            buttonMyContactsDelete.tag = contact

            textViewMyContactsUserName.text = contact.name
            textViewMyContactsUserCareer.text = contact.career
            if (contact.avatar.isNotBlank()) {
                Glide.with(imageViewMyContactsUserAvatar.context)
                    .load(contact.avatar)
                    .circleCrop()
                    .placeholder(R.drawable.default_user_photo)
                    .error(R.drawable.default_user_photo)
                    .into(imageViewMyContactsUserAvatar)
            } else {
                imageViewMyContactsUserAvatar.setImageResource(R.drawable.default_user_photo)
            }
        }
    }

    class ContactViewHolder(
        val binding: ContactItemBinding
    ) : RecyclerView.ViewHolder(binding.root)
}

//        fun bind(contact: Contact) {
//            binding.textViewMyContactsUserName.text = contact.name
//            binding.textViewMyContactsUserCareer.text = contact.career
//
//            Glide.with(binding.root)
//                .load(contact.avatar)
//                .circleCrop() // Performs image processing in the form of a circle
//                .placeholder(R.drawable.default_user_photo) // A placeholder image that is displayed until the image is loaded
//                .error(R.drawable.default_user_photo) // The image that is displayed in case of a download error
//                .into(binding.imageViewMyContactsUserAvatar)
//
//            binding.buttonMyContactsDelete.setOnClickListener {
//                val position = bindingAdapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    deleteContact(position)
//                }
////                showUndoSnackbar(contact, position)
//            }
//        }


//    fun showUndoSnackbar(contact: Contact, position: Int) {
//        val snackbar = Snackbar.make(
//            applicationContext,
//            context.getString(R.string.contact_removed),
//            Snackbar.LENGTH_LONG
//        )
//        snackbar.setAction(context.getString(R.string.cancel)) {
//            undoDeleteContact(contact, position)
//        }
//        snackbar.show()
//
//        // Automatically close the Snackbar after 5 seconds
//        val handler = Handler(Looper.getMainLooper())
//        val runnable = Runnable { snackbar.dismiss() }
//        handler.postDelayed(runnable, 5000)
//    }

//    private fun undoDeleteContact(contact: Contact, position: Int) {
//        contacts.add(position, contact)
//        notifyItemInserted(position)
//    }

//    override fun getItemCount(): Int {
//        return contacts.size
//    }
//
//    fun deleteContact(position: Int) {
//        contacts.removeAt(position)
//        notifyItemRemoved(position)
//    }
//
//    // Getting a contact by position
//    fun getContactAtPosition(position: Int): Contact {
//        return contacts[position]
//    }