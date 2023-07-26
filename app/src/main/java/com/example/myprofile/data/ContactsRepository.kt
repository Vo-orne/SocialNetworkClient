package com.example.myprofile.data

import android.content.Context
import com.example.myprofile.utils.ContactsContentProvider
import com.example.myprofile.utils.ext.UsersListener
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

    private var contacts = mutableListOf<Contact>()

    private val listeners = mutableListOf<UsersListener>()

    private lateinit var lastDeletedContact: Contact
    private var positionLastDeletedContact = 0
    private var isPhoneContactsAllowed = false

    private var lastId: Long = 0L

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
        return (1..NUM_RANDOM_CONTACTS).map {
            Contact(
                id = getId(),
                name = faker.name().name(),
                career = faker.job().field(),
                avatar = AVATARS[it % AVATARS.size],
                address = faker.address().fullAddress()
            )
        }
    }
    /**
     * Returns a unique identifier for a contact.
     */
    private fun getId(): Long {
        val result = lastId
        lastId++
        return result
    }
    /**
     * Deletes a contact from the contacts list at a specific index. It stores the deleted contact
     * in lastDeletedContact and its position in positionLastDeletedContact.
     * Then, it notifies listeners about the changes.
     * @param contact The contact to be deleted.
     * @param position The position of the contact to be deleted in the list.
     */
    fun deleteContact(contact: Contact, position: Int) {
        val indexToDelete = contacts.indexOfFirst {
            it.id == contact.id
        }
        if (indexToDelete != -1) {
            contacts = ArrayList(contacts)
            contacts.removeAt(position)
            lastDeletedContact = contact
            positionLastDeletedContact = position
            notifyChanges()
        }
    }
    /**
     * Restores the last deleted contact in the contacts list at position positionLastDeletedContact.
     * Then, it notifies listeners about the changes.
     */
    fun restoreLastDeletedContact() {
        contacts = ArrayList(contacts)
        contacts.add(positionLastDeletedContact, lastDeletedContact)
        notifyChanges()
    }
    /**
     * Adds a new contact to the contacts list and notifies listeners about the changes.
     * @param contact The contact to be added.
     */
    fun addContact(contact: Contact) {
        contacts = ArrayList(contacts)
        val newContact =
            Contact(getId(), contact.avatar, contact.name, contact.career, contact.address)
        contacts.add(contacts.size, newContact)
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
        private const val NUM_RANDOM_CONTACTS = 10
        private val AVATARS = mutableListOf(
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