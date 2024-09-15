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
import com.example.myprofile.presentation.utils.ext.loadImage
import com.example.myprofile.presentation.utils.ext.visibleIf

/**
 * Adapter for displaying a list of contacts in a RecyclerView.
 * @property listener The listener for handling contact actions (click, long click, delete).
 */
class ContactsAdapter(
    private val listener: ContactActionListener
) : ListAdapter<Contact, ContactsAdapter.ContactViewHolder>(UsersDiffCallback()) {

    /**
     * Set of selected contacts in multi-select mode.
     */
    private val selectedItems = HashSet<Pair<Contact, Int>>()

    /**
     * Flag indicating whether multi-select mode is enabled.
     */
    var isSelectMode = false

    /**
     * Creates a new ViewHolder and associates it with the layout structure from the layout file.
     * @param parent The parent view group.
     * @param viewType The type of view to create.
     * @return A new instance of ContactViewHolder.
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
     * Binds data to the ViewHolder for a specific position in the RecyclerView.
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    /**
     * ViewHolder for displaying a contact item in the RecyclerView.
     */
    @SuppressLint("NotifyDataSetChanged")
    inner class ContactViewHolder(
        private val binding: ContactItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the contact data to the ViewHolder.
         * @param contact The contact to display.
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

        /**
         * Sets up listeners for contact item actions.
         * @param contact The contact associated with the item.
         */
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
     * Toggles the selection of a contact in multi-select mode.
     * @param contact The contact to toggle.
     * @param position The position of the contact in the list.
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
     * Clears the list of selected contacts and exits multi-select mode.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun clearSelection() {
        selectedItems.clear()
        isSelectMode = false
        notifyDataSetChanged()
    }

    /**
     * Returns the set of selected contacts in multi-select mode.
     * @return The set of selected contacts.
     */
    fun getSelectedItems(): HashSet<Pair<Contact, Int>> {
        return selectedItems
    }

    /**
     * Sets the multi-select mode state.
     * @param it Boolean indicating whether to enable or disable multi-select mode.
     */
    fun setMultiselect(it: Boolean?) {
        if (it == true) {
            isSelectMode = true
        } else {
            clearSelection()
        }
    }
}