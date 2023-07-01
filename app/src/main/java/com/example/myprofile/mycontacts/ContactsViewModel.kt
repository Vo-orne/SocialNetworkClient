package com.example.myprofile.mycontacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactsViewModel: ViewModel() {
    private val contactsRepository = ContactsRepository() // Repository for receiving user data

    private val _contactsList = MutableLiveData<List<Contact>>() // Live data for saving the list of users
    val contactsList: LiveData<List<Contact>> = _contactsList // Public access to live data

    fun loadContacts(contactsPhoneBook: MutableList<Contact>) {
        val contacts = contactsRepository.getUsers() // Get the list of users from the repository
        contacts.addAll(contactsPhoneBook)
        _contactsList.value = contacts // Update live data values
    }

}