package com.example.myprofile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.R
import com.example.myprofile.data.Contact
import com.example.myprofile.databinding.ContactItemBinding
import com.example.myprofile.utils.ext.loadImage

// TODO: remove from this class
/**
 * Interface for the listener of contact actions in the adapter.
 */
interface ContactActionListener {
    fun onContactDelete(contact: Contact, position: Int)
    fun onDetailView(contact: Contact)
}
// TODO: optimize diff util and remove from this class
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

/**
 * Adapter for the list of contacts.
 * @property actionListener The listener for contact actions.
 */
class ContactsAdapter(
    private val actionListener: ContactActionListener
) : ListAdapter<Contact, ContactsAdapter.ContactViewHolder>(UsersDiffCallback()) {

    /**
     * Method called when an item in the list is clicked
     */


    /**
     * Method that creates new ViewHolders and associates them with the layout structure from the layout file
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ContactItemBinding.inflate(inflater, parent, false)

        return ContactViewHolder(binding)
    }

    /**
     * Method that binds data to the ViewHolder for a specific position in the RecyclerView
     */
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    /**
     * ViewHolder for the list of contacts
     */
    inner class ContactViewHolder(
        private val binding: ContactItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(contact: Contact) {
            with(binding) {
                itemView.tag = contact
                buttonMyContactsDelete.tag = contact

                textViewMyContactsUserName.text = contact.name
                textViewMyContactsUserCareer.text = contact.career
                if (contact.avatar.isNotBlank()) {
                    imageViewMyContactsUserAvatar.loadImage(contact.avatar)
                } else {
                    imageViewMyContactsUserAvatar.setImageResource(R.drawable.default_user_photo)
                }
            }
            setListener(contact)
        }

        private fun setListener(contact: Contact) {
            binding.buttonMyContactsDelete.setOnClickListener {
                actionListener.onContactDelete(contact, bindingAdapterPosition)
            }
            binding.root.setOnClickListener {
                actionListener.onDetailView(contact)
            }
        }
    }
}
