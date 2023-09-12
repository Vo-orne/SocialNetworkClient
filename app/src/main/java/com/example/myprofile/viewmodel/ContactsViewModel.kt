package com.example.myprofile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myprofile.data.Contact
import com.example.myprofile.data.ContactsRepository
import com.example.myprofile.utils.ext.UsersListener

class ContactsViewModel(private val contactsRepository: ContactsRepository): ViewModel() {

    private val _contacts = MutableLiveData<List<Contact>>() // Live data for saving the list of users
    val contacts: LiveData<List<Contact>> = _contacts // Public access to live data

    private val _selectedContacts = mutableListOf<Pair<Contact, Int>>()

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

    fun deleteSelectedContacts(selectedContacts: HashSet<Pair<Contact, Int>>) {
        contactsRepository.deleteSelectedContacts(selectedContacts)
    }

    fun deleteUser(user: Contact, position: Int) {
        contactsRepository.deleteContact(user, position)
    }

    fun restoreLastDeletedContact() {
        contactsRepository.restoreLastDeletedContact()
    }
}