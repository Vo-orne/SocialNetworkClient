package com.example.myprofile.presentation.ui.fragments.contacts.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.ContactItemBinding
import com.example.myprofile.presentation.ui.fragments.contacts.adapter.interfaces.ContactActionListener
import com.example.myprofile.presentation.ui.fragments.contacts.adapter.utils.UsersDiffCallback
import com.example.myprofile.presentation.utils.utils.ext.loadImage
import com.example.myprofile.presentation.utils.utils.ext.visibleIf

// TODO: if multiselect - swipe to delete: isEnabled false

/**
 * Adapter for the list of contacts.
 * @property listener The listener for contact actions.
 */
class ContactsAdapter(
    private val listener: ContactActionListener
) : ListAdapter<Contact, ContactsAdapter.ContactViewHolder>(UsersDiffCallback()) {

    /**
     * Selected contacts in multiselect mode.
     */
    private val selectedItems = HashSet<Pair<Contact, Int>>()

    /**
     * Multiselect state.
     */
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
    @SuppressLint("NotifyDataSetChanged")
    inner class ContactViewHolder(
        private val binding: ContactItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Displaying data in the contact.
         * @param contact A specific contact whose data will be displayed.
         */
        fun onBind(contact: Contact) {
            with(binding) {
                textViewContactItemUserName.text = contact.name
                textViewContactItemUserCareer.text = contact.career
                imageViewContactItemUserAvatar.loadImage(contact.avatar)
                imageViewContactItemSelectMode.isChecked = selectedItems.contains(Pair(contact, bindingAdapterPosition))
                imageViewContactItemSelectMode.visibleIf(isSelectMode)
                buttonContactItemDelete.visibility = if (isSelectMode) View.GONE else View.VISIBLE
            }
            setListeners(contact)
        }

        private fun setListeners(contact: Contact) {
            binding.root.setOnClickListener {
                listener.onClick(contact, bindingAdapterPosition)
            }
            binding.root.setOnLongClickListener {
                if (!isSelectMode) {
                    selectedItems.clear()
                    listener.onLongClick(contact, bindingAdapterPosition)
                    notifyDataSetChanged()
                }
                true
            }
            binding.buttonContactItemDelete.setOnClickListener {
                listener.onContactDelete(contact, bindingAdapterPosition)
            }
        }
    }

    /**
     * Switching the selection of a contact in multiselect mode.
     */
    fun toggleSelection(contact: Contact, position: Int) {
        if (selectedItems.contains(Pair(contact, position))) {
            selectedItems.remove(Pair(contact, position))
        } else {
            selectedItems.add(Pair(contact, position))
        }
        notifyItemChanged(currentList.indexOf(contact))
    }

    /**
     * Completely clears the list of selected contacts in multiselect mode and exits it.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun clearSelection() {
        selectedItems.clear()
        isSelectMode = false
        notifyDataSetChanged()
    }

    /**
     * Returns the list of selected contacts in multiselect mode.
     */
    fun getSelectedItems(): HashSet<Pair<Contact, Int>> {
        return selectedItems
    }

    fun setMultiselect(it: Boolean?) {
        if (it == true) {
            isSelectMode = true
        } else {
            clearSelection()
        }
    }
}