package com.example.myprofile.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.R
import com.example.myprofile.data.Contact
import com.example.myprofile.databinding.ContactItemBinding
import com.example.myprofile.utils.ext.loadImage


/**
 * Adapter for the list of contacts.
 * @property actionListener The listener for contact actions.
 */
class ContactsAdapter(
    private val actionListener: ContactActionListener
) : ListAdapter<Contact, ContactsAdapter.ContactViewHolder>(UsersDiffCallback()) {

    private val selectedItems = HashSet<Contact>()
    var isSelectMode = false


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

        init {
            binding.root.setOnClickListener {
                val contact = currentList[bindingAdapterPosition]
//                if (isSelectMode) {
//                    toggleSelection(contact)
//                } else {
//                    actionListener.onClick(contact, bindingAdapterPosition)
//                }
                actionListener.onClick(contact, bindingAdapterPosition)
            }

            binding.root.setOnLongClickListener {
                val contact = currentList[bindingAdapterPosition]
                if (!isSelectMode) {
                    isSelectMode = true
                    selectedItems.clear()
//                    selectedItems.add(contact)
                    actionListener.onLongClick(contact, bindingAdapterPosition)
                    notifyDataSetChanged()
                }
                true
            }

            binding.buttonContactItemDelete.setOnClickListener {
                val contact = currentList[bindingAdapterPosition]
                actionListener.onContactDelete(contact, bindingAdapterPosition)
            }
        }

        fun onBind(contact: Contact) {
            with(binding) {
                itemView.tag = contact
                textViewContactItemUserName.text = contact.name
                textViewContactItemUserCareer.text = contact.career
                if (contact.avatar.isNotBlank()) {
                    imageViewContactItemUserAvatar.loadImage(contact.avatar)
                } else {
                    imageViewContactItemUserAvatar.setImageResource(R.drawable.default_user_photo)
                }
                imageViewContactItemSelectMode.isChecked = selectedItems.contains(contact)
                imageViewContactItemSelectMode.visibility = if (isSelectMode) View.VISIBLE else View.GONE
                buttonContactItemDelete.visibility = if (isSelectMode) View.GONE else View.VISIBLE
            }
        }
    }

    fun toggleSelection(contact: Contact) {
        if (selectedItems.contains(contact)) {
            selectedItems.remove(contact)
        } else {
            selectedItems.add(contact)
        }
        notifyItemChanged(currentList.indexOf(contact))
    }

    fun clearSelection() {
        selectedItems.clear()
        isSelectMode = false
        notifyDataSetChanged()
    }

    fun getSelectedItems(): Set<Contact> {
        return selectedItems
    }
}