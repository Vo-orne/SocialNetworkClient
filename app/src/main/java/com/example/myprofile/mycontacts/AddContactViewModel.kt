package com.example.myprofile.mycontacts

import androidx.lifecycle.ViewModel

class AddContactViewModel(private val contactsRepository: ContactsRepository): ViewModel() {

    fun addContact(contact: Contact) {
        contactsRepository.addContact(contact)
    }
}