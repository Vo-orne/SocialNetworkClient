package com.example.myprofile.mycontacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactsViewModel: ViewModel() {
    private val contactsRepository = ContactsRepository() // Repository for receiving user data

    private val _contactsList = MutableLiveData<List<Contact>>() // Live data for saving the list of users
    val contactsList: LiveData<List<Contact>> = _contactsList // Public access to live data

    fun loadContacts(contactsPhoneBook: MutableList<Contact>) {
        val contacts = contactsRepository.getUsers() // Get the list of users from the repository
        contacts.addAll(contactsPhoneBook)
        _contactsList.value = contacts // Update live data values
    }

}

class ContactsRepository {
    fun getUsers(): MutableList<Contact> {
        val contacts = mutableListOf<Contact>()

        // Generate contacts
        for (i in 1..10) {
            val avatar = getRandomAvatar()
            val name = generateRandomName()
            val career = generateRandomCareer()
            val contact = Contact(avatar, name, career)
            contacts.add(contact)
        }

        return contacts
    }

    private fun generateRandomName(): String {
        val names = listOf("John", "Emma", "Michael", "Sophia", "James", "Olivia")
        val surnames = listOf("Smith", "Johnson", "Brown", "Taylor", "Miller")
        val randomName = names.random()
        val randomSurname = surnames.random()
        return "$randomName $randomSurname"
    }

    private fun generateRandomCareer(): String {
        val careers = listOf("Engineer", "Teacher", "Doctor", "Designer", "Developer", "Writer")
        return careers.random()
    }

    private fun getRandomAvatar() = avatars.random()

    private val avatars = mutableListOf(
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
