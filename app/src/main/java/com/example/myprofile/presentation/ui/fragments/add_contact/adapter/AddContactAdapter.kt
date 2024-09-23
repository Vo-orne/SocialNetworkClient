package com.example.myprofile.presentation.ui.fragments.add_contact.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.UserItemBinding
import com.example.myprofile.domain.states.ApiState
import com.example.myprofile.presentation.ui.fragments.add_contact.adapter.interfaces.AddContactActionListener
import com.example.myprofile.presentation.ui.fragments.contacts.adapter.utils.UsersDiffCallback
import com.example.myprofile.presentation.utils.ext.gone
import com.example.myprofile.presentation.utils.ext.invisible
import com.example.myprofile.presentation.utils.ext.log
import com.example.myprofile.presentation.utils.ext.visible
import com.example.myprofile.presentation.utils.ext.loadImage

/**
 * Adapter for displaying contacts in the AddContactFragment.
 *
 * @param listener A callback listener for add contact actions.
 */
class AddContactsAdapter(
    private val listener: AddContactActionListener,
) : ListAdapter<Contact, AddContactsAdapter.UsersViewHolder>(UsersDiffCallback()) {

    /**
     * List of states associated with contacts.
     */
    private var states: ArrayList<Pair<Long, ApiState>> = ArrayList()

    /**
     * Progress bar to display download status.
     */
    private lateinit var progressBar: ProgressBar

    /**
     * Creates and returns a new UsersViewHolder.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsersViewHolder {
        // Inflate the item layout and create a ViewHolder instance.
        val inflater = LayoutInflater.from(parent.context)
        val binding = UserItemBinding.inflate(inflater, parent, false)
        progressBar = binding.progressBar
        return UsersViewHolder(binding)
    }

    /**
     * Binds the contact and its state to the UsersViewHolder.
     */
    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        // Bind the contact data and state to the ViewHolder.
        holder.bind(
            currentList[position],
            states.find { it.first == currentList[position].id }?.second ?: ApiState.Initial
        )
    }

    inner class UsersViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the contact and its state to the ViewHolder.
         *
         * @param contact The contact to be displayed.
         * @param state The state associated with the contact.
         */
        fun bind(contact: Contact, state: ApiState) {
            with(binding) {
                // Set contact details in the views.
                textViewUserItemUserName.text = contact.name
                textViewUserItemUserCareer.text = contact.career
                imageViewUserItemUserAvatar.loadImage(contact.avatar)
            }
            setState(state)
            setListeners(contact)
        }

        /**
         * Updates the visibility of UI elements based on the state.
         *
         * @param state The current state of the contact.
         */
        private fun setState(state: ApiState) {
            with(binding) {
                when (state) {
                    is ApiState.Success<*> -> {
                        // Contact has been successfully added.
                        textViewUserItemUserAdd.gone()
                        imageViewUserItemPlus.gone()
                        progressBar.gone()
                        imageViewUserItemSelected.visible()
                    }
                    is ApiState.Initial -> {
                        // Contact is in its initial state.
                        textViewUserItemUserAdd.visible()
                        imageViewUserItemPlus.visible()
                        progressBar.invisible()
                    }
                    is ApiState.Loading -> {
                        // Contact is being processed (loading state).
                        textViewUserItemUserAdd.invisible()
                        imageViewUserItemPlus.invisible()
                        progressBar.visible()
                        imageViewUserItemSelected.invisible()
                    }
                    is ApiState.Error -> {
                        // An error occurred during processing.
                        progressBar.invisible()
                        log(state)
                    }
                }
            }
        }

        /**
         * Sets click listeners for adding a contact.
         *
         * @param contact The contact to be added.
         */
        private fun setListeners(contact: Contact) {
            addContact(contact)
        }

        /**
         * Sets click listeners for the add contact buttons.
         *
         * @param contact The contact to be added.
         */
        private fun addContact(contact: Contact) {
            with(binding) {
                textViewUserItemUserAdd.setOnClickListener {
                    listener.onClickAddButton(contact, bindingAdapterPosition)
                }
                imageViewUserItemPlus.setOnClickListener {
                    listener.onClickAddButton(contact, bindingAdapterPosition)
                }
            }
        }
    }

    /**
     * Updates the states for the contacts in the adapter.
     *
     * @param states A list of states associated with contacts.
     */
    fun setStates(states: ArrayList<Pair<Long, ApiState>>) {
        if (this.states.size != states.size) {
            this.states = states
            val lastIndex = currentList.indexOfLast { it.id == states.lastOrNull()?.first }
            if (lastIndex != -1) {
                notifyItemChanged(lastIndex)
            }
            return
        }
        states.forEachIndexed { index, state ->
            if (this.states[index] != states[index]) {
                this.states[index] = state
                notifyItemChanged(currentList.indexOfFirst { it.id == state.first })
            }
        }
    }
}
