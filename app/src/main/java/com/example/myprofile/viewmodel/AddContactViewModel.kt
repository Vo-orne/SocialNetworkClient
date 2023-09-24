package com.example.myprofile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.repository.ContactsRepository

class AddContactViewModel(private val contactsRepository: ContactsRepository): ViewModel() {

    fun addContact(contact: Contact) {
        contactsRepository.addContact(contact)
    }
}