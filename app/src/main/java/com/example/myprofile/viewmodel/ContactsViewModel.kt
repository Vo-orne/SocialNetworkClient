package com.example.myprofile.viewmodel

import android.util.Log
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
    val selectedContacts: List<Pair<Contact, Int>> by lazy { _selectedContacts }

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

    fun addSelectedContact(contact: Contact, position: Int) {
        _selectedContacts.add(Pair(contact, position))
        val x = _selectedContacts.size.toString()
        Log.d("myLog", "_selectedContacts.size in VM = $x")
    }

    fun removeSelectedContact(contact: Contact, position: Int) {
        _selectedContacts.remove(Pair(contact, position))
        val x = _selectedContacts.size.toString()
        Log.d("myLog", "_selectedContacts.size in VM = $x")
    }

    private fun loadContacts() {
        contactsRepository.addListener(listener)
    }

    fun deleteSelectedContacts(selectedContacts: Set<Contact>) {
        contactsRepository.deleteSelectedContacts(selectedContacts)
    }

    fun deleteSelectedContacts() {
        contactsRepository.deleteSelectedContacts(_selectedContacts)
        _selectedContacts.clear()
    }

    fun deleteUser(user: Contact, position: Int) {
        contactsRepository.deleteContact(user, position)
    }

    fun restoreLastDeletedContact() {
        contactsRepository.restoreLastDeletedContact()
    }
}