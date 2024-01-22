package com.example.myprofile.presentation.ui.fragments.add_contact.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.UserItemBinding
import com.example.myprofile.domain.ApiState
import com.example.myprofile.presentation.ui.fragments.add_contact.adapter.interfaces.AddContactActionListener
import com.example.myprofile.presentation.ui.fragments.contacts.adapter.utils.UsersDiffCallback
import com.example.myprofile.presentation.utils.ext.gone
import com.example.myprofile.presentation.utils.ext.invisible
import com.example.myprofile.presentation.utils.ext.log
import com.example.myprofile.presentation.utils.ext.visible
import com.example.myprofile.presentation.utils.utils.ext.loadImage

class AddContactsAdapter(
    private val listener: AddContactActionListener,
) : ListAdapter<Contact, AddContactsAdapter.UsersViewHolder>(UsersDiffCallback()) {

    private var states: ArrayList<Pair<Long, ApiState>> = ArrayList()
    private lateinit var progressBar: ProgressBar

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = UserItemBinding.inflate(inflater, parent, false)
        progressBar = binding.progressBar
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(
            currentList[position],
            states.find { it.first == currentList[position].id }?.second ?: ApiState.Initial
        )
    }

    inner class UsersViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact, state: ApiState) {
            with(binding) {
                textViewUserItemUserName.text = contact.name
                textViewUserItemUserCareer.text = contact.career
                imageViewUserItemUserAvatar.loadImage(contact.avatar)
            }
            setState(state)
            setListeners(contact)
        }

        private fun setState(state: ApiState) {
            with(binding) {
                when (state) {
                    is ApiState.Success<*> -> {
                        textViewUserItemUserAdd.gone()
                        imageViewUserItemPlus.gone()
                        progressBar.gone()
                        imageViewUserItemSelected.visible()
                    }
                    is ApiState.Initial -> {
                        textViewUserItemUserAdd.visible()
                        imageViewUserItemPlus.visible()
                        progressBar.invisible()
                    }
                    is ApiState.Loading -> {
                        textViewUserItemUserAdd.invisible()
                        imageViewUserItemPlus.invisible()
                        progressBar.visible()
                        imageViewUserItemSelected.invisible()
                    }
                    is ApiState.Error -> {
                        progressBar.invisible()
                        log(state)
                    }
                }
            }
        }

        private fun setListeners(contact: Contact) {
            addContact(contact)
//            detailView(contact)
        }

//        private fun detailView(contact: Contact) {
//            with(binding) {
//                root.setOnClickListener {
//                    listener.onClickContact(
//                        contact, arrayOf(
//                            setTransitionName(
//                                imageViewUserItemUserAvatar,
//                                Constants.TRANSITION_NAME_IMAGE + contact.id
//                            ),
//                            setTransitionName(
//                                textViewUserItemUserName,
//                                Constants.TRANSITION_NAME_CONTACT_NAME + contact.id
//                            ), setTransitionName(
//                                textViewUserItemUserCareer,
//                                Constants.TRANSITION_NAME_CAREER + contact.id
//                            )
//                        )
//                    )
//                }
//            }
//        }

        private fun addContact(contact: Contact) {
            with(binding) {
                textViewUserItemUserAdd.setOnClickListener {
                    listener.onClickAddButton(contact, bindingAdapterPosition)
//                    textViewUserItemUserAdd.gone()
//                    imageViewUserItemPlus.gone()
//                    imageViewUserItemSelected.visible()
                }
                imageViewUserItemPlus.setOnClickListener {
                    listener.onClickAddButton(contact, bindingAdapterPosition)
//                    textViewUserItemUserAdd.gone()
//                    imageViewUserItemPlus.gone()
//                    imageViewUserItemSelected.visible()
                }
//                imageViewUserItemSelected.setOnClickListener {
//                    imageViewUserItemSelected.gone()
//                    textViewUserItemUserAdd.visible()
//                    imageViewUserItemPlus.visible()
//                }
            }
        }

//        private fun setTransitionName(view: View, name: String): Pair<View, String> {
//            view.transitionName = name
//            return view to name
//        }
    }

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