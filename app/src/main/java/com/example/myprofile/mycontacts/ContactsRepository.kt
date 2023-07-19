package com.example.myprofile.mycontacts

import android.content.Context
import com.github.javafaker.Faker

typealias UsersListener = (users: List<Contact>) -> Unit

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

    private fun loadContacts() {
        contacts = if (isPhoneContactsAllowed) {
            val contentProvider = ContactsContentProvider.getInstance(context)
            (contentProvider.getPhoneContacts() + generateRandomContacts()).toMutableList()
        } else {
            generateRandomContacts().toMutableList()
        }
    }

    fun allowPhoneContacts() {
        isPhoneContactsAllowed = true
        val contentProvider = ContactsContentProvider.getInstance(context)
        val contacts = (contentProvider.getPhoneContacts() + generateRandomContacts()).toMutableList()
        this.contacts = ArrayList(contacts)
        notifyChanges()
    }

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

    private fun getId(): Long {
        val result = lastId
        lastId++
        return result
    }

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


    fun restoreLastDeletedContact() {
        contacts = ArrayList(contacts)
        contacts.add(positionLastDeletedContact, lastDeletedContact)
        notifyChanges()
    }

    fun addContact(contact: Contact) {
        contacts = ArrayList(contacts)
        val newContact = Contact(getId(), contact.avatar, contact.name, contact.career, contact.address)
        contacts.add(contacts.size, newContact)
        notifyChanges()
    }

    fun addListener(listener: UsersListener) {
        listeners.add(listener)
        listener.invoke(contacts)
    }

    fun removeListener(listener: UsersListener) {
        listeners.remove(listener)
    }

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