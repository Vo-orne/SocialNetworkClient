package com.example.myprofile.mycontacts

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.github.javafaker.Faker

typealias UsersListener = (users: List<Contact>) -> Unit

class ContactsRepository(private val context: Context) {

    private var contacts = mutableListOf<Contact>()

    private val listeners = mutableListOf<UsersListener>()

    init {
        loadContacts()
    }

    private fun loadContacts() {
        val phoneContacts = getPhoneContacts()
        val randomContacts = generateRandomContacts()
        contacts = (phoneContacts + randomContacts).toMutableList()
        notifyChanges()
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
            val careerColumnIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE)
            val avatarColumnIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)

            if (idColumnIndex == -1 || nameColumnIndex == -1 || careerColumnIndex == -1 || avatarColumnIndex == -1) {
                // Якщо один зі стовпців не знайдений, ви можете виконати відповідні дії, наприклад, вивести повідомлення про помилку
                return parsedContacts
            }

            while (it.moveToNext()) {
                val id = it.getString(idColumnIndex)?.toLongOrNull() ?: continue
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
        return (1..NUM_RANDOM_CONTACTS).map { Contact(
            id = it.toLong(),
            name = faker.name().name(),
            career = faker.job().field(),
            avatar = AVATARS[it % AVATARS.size]
        ) }
    }

    fun deleteUser(user: Contact) {
        val indexToDelete = contacts.indexOfFirst { it.id == user.id }
        if (indexToDelete != -1) {
            contacts.removeAt(indexToDelete)
            notifyChanges()
        }
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