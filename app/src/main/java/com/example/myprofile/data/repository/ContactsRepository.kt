package com.example.myprofile.data.repository

import android.content.Context
import com.example.myprofile.data.model.Contact
import com.example.myprofile.presentation.utils.ContactsContentProvider
import com.example.myprofile.presentation.utils.utils.ext.UsersListener
import com.github.javafaker.Faker

/**
 * The ContactsRepository class represents the data repository responsible for managing
 * the contact data within the MyProfile app. It interacts with the ContactsContentProvider
 * to fetch phone contacts and generates random contacts using the Faker library.
 * It also handles adding, deleting, and restoring contacts. The repository uses a list of
 * Contact objects to store the contact data.
 * @param context The application context required to access resources and the content provider.
 */
class ContactsRepository(private val context: Context) {

    /**
     * List of contacts to be displayed.
     */
    private var contacts = mutableListOf<Contact>()

    /**
     * List of listeners.
     */
    private val listeners = mutableListOf<UsersListener>()

    /**
     * The user's response to the request to obtain permission to the phone's contact list.
     */
    private var isPhoneContactsAllowed = false

    /**
     * A list of the last saved contacts that will be possible to return.
     */
    private var _lastDeletedContacts = mutableListOf<Pair<Contact, Int>>()

    init {
        loadContacts()
    }

    /**
     * Loads the contacts. If access to phone contacts is allowed, it fetches the contacts
     * from the ContactsContentProvider, adding random contacts to them, and stores in the contacts list.
     * Otherwise, it simply generates random contacts and stores them in the contacts list.
     */
    private fun loadContacts() {
        contacts = if (isPhoneContactsAllowed) {
            val contentProvider = ContactsContentProvider.getInstance(context)
            (contentProvider.getPhoneContacts() + generateRandomContacts()).toMutableList()
        } else {
            generateRandomContacts().toMutableList()
        }
    }

    /**
     * Allows access to phone contacts. It fetches the contacts from the ContactsContentProvider,
     * adding random contacts to them, and replaces the contacts list with the new list containing these contacts.
     * It also notifies listeners about the changes.
     */
    fun allowPhoneContacts() {
        isPhoneContactsAllowed = true
        val contentProvider = ContactsContentProvider.getInstance(context)
        val contacts = (contentProvider.getPhoneContacts() + generateRandomContacts()).toMutableList()
        this.contacts = ArrayList(contacts)
        notifyChanges()
    }

    /**
     * Generates random contacts using the Faker library and returns a list of contacts.
     */
    private fun generateRandomContacts(): List<Contact> {
        val faker = Faker.instance()
        return (1..AVATARS.size).map {
            Contact(
                name = faker.name().name(),
                career = faker.job().field(),
                avatar = AVATARS[it % AVATARS.size],
                address = faker.address().fullAddress()
            )
        }
    }

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
     * Deletes all selected contacts in multiselect mode.
     * @param selectedContacts List of selected contacts transferred View model.
     */
    fun deleteSelectedContacts(selectedContacts: HashSet<Pair<Contact, Int>>) {
        // Clears previously deleted contacts so that _lastDeletedContacts contains only
        // the last deleted contacts.
        _lastDeletedContacts.clear()
        val deletedContacts = mutableListOf<Pair<Contact, Int>>()

        // Sort selected Contacts in descending index order,
        // to delete contacts with the current index
        selectedContacts.sortedByDescending { it.second }
        for (contact in selectedContacts) {
            val indexToDelete = contacts.indexOfFirst { it.id == contact.first.id }
            if (indexToDelete != -1) {
                contacts = ArrayList(contacts)
                contacts.removeAt(indexToDelete)
                deletedContacts.add(Pair(contact.first, contact.second))
            }
        }
        _lastDeletedContacts.addAll(deletedContacts)
        notifyChanges()
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
            _lastDeletedContacts.add(Pair(contact, indexToDelete))
            notifyChanges()
        }
    }



    /**
     * Restores the last deleted contact in the contacts list at position positionLastDeletedContact.
     * Then, it notifies listeners about the changes.
     */
    fun restoreLastDeletedContact() {
        // Sort _lastDeletedContacts in ascending index order,
        // to restore contacts by the current index
        _lastDeletedContacts.sortBy { it.second }

        contacts = ArrayList(contacts)
        for (contact in _lastDeletedContacts) {
            contacts.add(contact.second, contact.first)
        }
        notifyChanges()
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

    companion object {
        private val AVATARS = listOf(
            "https://randomuser.me/api/portraits/women/44.jpg",
            "https://randomuser.me/api/portraits/men/46.jpg",
            "https://randomuser.me/api/portraits/men/97.jpg",
            "https://randomuser.me/api/portraits/men/84.jpg",
            "https://randomuser.me/api/portraits/women/63.jpg",
            "https://randomuser.me/api/portraits/men/86.jpg",
            "https://randomuser.me/api/portraits/women/95.jpg",
            "https://api.uifaces.co/our-content/donated/xZ4wg2Xj.jpg",
            "https://randomuser.me/api/portraits/women/30.jpg",
            "https://images.pexels.com/photos/449977/pexels-photo-449977.jpeg?h=350&auto=compress&cs=tinysrgb"
        )
    }
}