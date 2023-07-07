package com.example.myprofile.mycontacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactsViewModel(private val contactsRepository: ContactsRepository): ViewModel() {

    private val _contacts = MutableLiveData<List<Contact>>() // Live data for saving the list of users
    val contacts: LiveData<List<Contact>> = _contacts // Public access to live data

    private val listener: UsersListener = {
        _contacts.value = it
    }

    init {
        loadContacts()
    }

    override fun onCleared() {
        super.onCleared()
        contactsRepository.removeListener(listener)
    }

    private fun loadContacts() {
        contactsRepository.addListener(listener)
    }

    fun deleteUser(user: Contact) {
        contactsRepository.deleteUser(user)
    }
}