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

    private val listener: UsersListener = {
        _contacts.value = it
    }

    init {
        loadContacts()
    }

    /**
     * Removes all listeners after the program is finished.
     */
    override fun onCleared() {
        super.onCleared()
        contactsRepository.removeListener(listener)
    }

    /**
     * Getting a list of contacts.
     */
    private fun loadContacts() {
        contactsRepository.addListener(listener)
    }

    /**
     * Deletes all selected contacts in multiselect mode.
     * @param selectedContacts List of selected contacts transferred fragment.
     */
    fun deleteSelectedContacts(selectedContacts: HashSet<Pair<Contact, Int>>) {
        contactsRepository.deleteSelectedContacts(selectedContacts)
    }

    /**
     * Removes a specific contact from the contact list.
     * @param user The contact to be deleted from the contact list.
     * @param position The position of the contact to be removed from the contact list.
     */
    fun deleteUser(user: Contact, position: Int) {
        contactsRepository.deleteContact(user, position)
    }

    /**
     * Returns the last deleted contacts that were deleted from the contact list back.
     */
    fun restoreLastDeletedContact() {
        contactsRepository.restoreLastDeletedContact()
    }
}