package com.example.myprofile.data.repository

import android.content.Context
import com.example.myprofile.data.model.Contact
import com.example.myprofile.domain.ApiState
import com.example.myprofile.presentation.utils.ext.UsersListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * The ContactsRepository class represents the data repository responsible for managing
 * the contact data within the MyProfile app. It interacts with the ContactsContentProvider
 * to fetch phone contacts and generates random contacts using the Faker library.
 * It also handles adding, deleting, and restoring contacts. The repository uses a list of
 * Contact objects to store the contact data.
 * @param context The application context required to access resources and the content provider.
 */
class ContactsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * List of contacts to be displayed.
     */
    private var contacts = mutableListOf<Contact>()

    /**
     * List of listeners.
     */
    private val listeners = mutableListOf<UsersListener>()

    /**
     * A list of the last saved contacts that will be possible to return.
     */
    private var _lastDeletedContacts = mutableListOf<Contact>()
    var lastDeletedContacts = _lastDeletedContacts


    /**
     * Deletes a contact from the contacts list at a specific index. It stores the deleted contact
     * in lastDeletedContact and its position in positionLastDeletedContact.
     * Then, it notifies listeners about the changes.
     * @param contact The contact to be deleted.
     * @param position The position of the contact to be deleted in the list.
     */
    fun deleteContact(contact: Contact, position: Int) {
        _lastDeletedContacts.clear()
        deleteItem(contact, position)
    }

    /**
     * Removes a specific contact from the contact list.
     * @param contact Contact to be deleted.
     * @param position The position of the contact in the contact list.
     */
    private fun deleteItem(contact: Contact, position: Int) {
        val indexToDelete = contacts.indexOfFirst { it.id == contact.id }
        if (indexToDelete != -1) {
            contacts = ArrayList(contacts)
            contacts.removeAt(position)
            _lastDeletedContacts.add(contact)
            notifyChanges()
        }
    }

//    /**
//     * Deletes all selected contacts in multiselect mode.
//     * @param selectedContacts List of selected contacts transferred View model.
//     */
//    fun deleteSelectedContacts(selectedContacts: HashSet<Pair<Contact, Int>>) {
//        // Clears previously deleted contacts so that _lastDeletedContacts contains only
//        // the last deleted contacts.
//        _lastDeletedContacts.clear()
//        val deletedContacts = mutableListOf<Pair<Contact, Int>>()
//
//        // Sort selected Contacts in descending index order,
//        // to delete contacts with the current index
//        selectedContacts.sortedByDescending { it.second }
//        for (contact in selectedContacts) {
//            val indexToDelete = contacts.indexOfFirst { it.id == contact.first.id }
//            if (indexToDelete != -1) {
//                contacts = ArrayList(contacts)
//                contacts.removeAt(indexToDelete)
//                deletedContacts.add(Pair(contact.first, contact.second))
//            }
//        }
//        _lastDeletedContacts.addAll(deletedContacts)
//        notifyChanges()
//    }

    fun addLastDeletedContacts(contact: Contact) {
        _lastDeletedContacts.add(contact)
    }


//    /**
//     * Restores the last deleted contact in the contacts list at position positionLastDeletedContact.
//     * Then, it notifies listeners about the changes.
//     */
//    fun restoreLastDeletedContact() {
//        // Sort _lastDeletedContacts in ascending index order,
//        // to restore contacts by the current index
//        _lastDeletedContacts.sortBy { it.second }
//
//        contacts = ArrayList(contacts)
//        for (contact in _lastDeletedContacts) {
//            contacts.add(contact.second, contact.first)
//        }
//        notifyChanges()
//        _lastDeletedContacts.clear()
//    }

    fun clearLastDeletedContact() {
        _lastDeletedContacts.clear()
    }

    /**
     * Adds a new contact to the contacts list and notifies listeners about the changes.
     * @param contact The contact to be added.
     */
    fun addContact(contact: Contact) {
        contacts = ArrayList(contacts)
        contacts.add(contacts.size, contact)
        notifyChanges()
    }

    /**
     * Adds a listener to the listeners list and notifies it about the current contacts list.
     * @param listener The listener to be added.
     */
    fun addListener(listener: UsersListener) {
        listeners.add(listener)
        listener.invoke(contacts)
    }

    /**
     * Removes a listener from the listeners list.
     * @param listener The listener to be removed.
     */
    fun removeListener(listener: UsersListener) {
        listeners.remove(listener)
    }

    /**
     * Notifies all listeners about the changes in the contacts list.
     */
    private fun notifyChanges() {
        listeners.forEach { it.invoke(contacts) }
    }
}