package com.example.myprofile.mycontacts

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.github.javafaker.Faker

typealias UsersListener = (users: List<Contact>) -> Unit

class ContactsRepository(private val context: Context) {

    private var contacts = mutableListOf<Contact>()

    private val listeners = mutableListOf<UsersListener>()

    private lateinit var lastDeletedContact: Contact
    private var positionLastDeletedContact = 0

    private var lastId: Long = 0L

    init {
        loadContacts()
    }

    private fun loadContacts() {
        val phoneContacts = getPhoneContacts()
        val randomContacts = generateRandomContacts()
        contacts = (phoneContacts + randomContacts).toMutableList()
    }

    private fun getPhoneContacts(): List<Contact> {
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        return parseCursor(cursor)
    }

    private fun parseCursor(cursor: Cursor?): List<Contact> {
        val parsedContacts = mutableListOf<Contact>()
        cursor?.use {
            val idColumnIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
            val nameColumnIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val careerColumnIndex =
                it.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE)
            val avatarColumnIndex =
                it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)

            if (idColumnIndex == -1 || nameColumnIndex == -1 || careerColumnIndex == -1 || avatarColumnIndex == -1) {
                // If one of the columns is not found, you can take appropriate actions, such as displaying an error message
                return parsedContacts
            }

            while (it.moveToNext()) {
                val id = getId()
                val name = it.getString(nameColumnIndex) ?: ""
                val career = it.getString(careerColumnIndex) ?: ""
                val avatar = it.getString(avatarColumnIndex) ?: ""

                val contact = Contact(id, avatar, name, career)
                parsedContacts.add(contact)
            }
        }
        return parsedContacts
    }

    private fun generateRandomContacts(): List<Contact> {
        val faker = Faker.instance()
        return (1..NUM_RANDOM_CONTACTS).map {
            Contact(
                id = getId(),
                name = faker.name().name(),
                career = faker.job().field(),
                avatar = AVATARS[it % AVATARS.size]
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
        val newContact = Contact(getId(), contact.avatar, contact.name, contact.career)
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