package com.example.myprofile.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.databinding.ContactItemBinding
import com.bumptech.glide.Glide
import com.example.myprofile.R
import com.example.myprofile.data.Contact

/**
 * Interface for the listener of contact actions in the adapter.
 */
interface ContactActionListener {
    fun onContactDelete(contact: Contact, position: Int)
    fun onDetailView(contact: Contact)
}

/**
 * The `UsersDiffCallback` class is used to calculate changes between two lists of contacts.
 */
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

/**
 * Adapter for the list of contacts.
 * @property actionListener The listener for contact actions.
 */
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

    /**
     * Method called when an item in the list is clicked
     */
    override fun onClick(v: View) {
        val contact = v.tag as Contact
        val position = contacts.indexOf(contact)
        when (v.id) {
            R.id.buttonMyContactsDelete -> {
                actionListener.onContactDelete(contact, position)
            }
            else -> {
                actionListener.onDetailView(contact)
            }
        }
    }

    override fun getItemCount(): Int = contacts.size

    /**
     * Method that creates new ViewHolders and associates them with the layout structure from the layout file
     */
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

    /**
     * Method that binds data to the ViewHolder for a specific position in the RecyclerView
     */
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

    /**
     * ViewHolder for the list of contacts
     */
    class ContactViewHolder(
        val binding: ContactItemBinding
    ) : RecyclerView.ViewHolder(binding.root)
}
