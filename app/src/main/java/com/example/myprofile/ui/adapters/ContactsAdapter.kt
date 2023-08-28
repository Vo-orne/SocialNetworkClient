package com.example.myprofile.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
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

    fun onDetailView(contact: Contact, position: Int)

    fun onLongClick(contact: Contact, position: Int)
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

    private var isSelectItems: ArrayList<Pair<Boolean, Int>> = ArrayList()


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
                buttonContactItemDelete.tag = contact

                textViewContactItemUserName.text = contact.name
                textViewContactItemUserCareer.text = contact.career
                if (contact.avatar.isNotBlank()) {
                    imageViewContactItemUserAvatar.loadImage(contact.avatar)
                } else {
                    imageViewContactItemUserAvatar.setImageResource(R.drawable.default_user_photo)
                }
            }
            setListener(contact)
        }

        private fun setListener(contact: Contact) {
            if (isSelectItems.isNotEmpty()) setSelectList(contact)
            with(binding) {
                buttonContactItemDelete.setOnClickListener {
                    actionListener.onContactDelete(contact, bindingAdapterPosition)
                }
                root.setOnClickListener {
                    if (isSelectItems.isNotEmpty()) {
                        imageViewContactItemSelectMode.isChecked = true
                    }
                    actionListener.onDetailView(contact, bindingAdapterPosition)
                }
                root.setOnLongClickListener {
                    imageViewContactItemSelectMode.isChecked = true
                    actionListener.onLongClick(contact, bindingAdapterPosition)
                    true
                }
            }
        }

        private fun setSelectList(contact: Contact) {
            with(binding) {
                imageViewContactItemSelectMode.visibility = View.VISIBLE
                buttonContactItemDelete.visibility = View.GONE
                imageViewContactItemSelectMode.isChecked =
                    isSelectItems.find { (contact.id.equals(it.second)) }?.first == true
                actionListener.onDetailView(contact, bindingAdapterPosition)
            }
        }
    }

    fun setMultiselectData(isSelectItems: ArrayList<Pair<Boolean, Int>>) {
        this.isSelectItems = isSelectItems
    }
}
