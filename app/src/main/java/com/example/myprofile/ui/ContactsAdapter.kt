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


interface ContactActionListener {
    fun onContactDelete(contact: Contact, position: Int)
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