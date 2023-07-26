package com.example.myprofile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myprofile.data.Contact
import com.example.myprofile.data.ContactsRepository

class AddContactViewModel(private val contactsRepository: ContactsRepository): ViewModel() {

    fun addContact(contact: Contact) {
        contactsRepository.addContact(contact)
    }
}