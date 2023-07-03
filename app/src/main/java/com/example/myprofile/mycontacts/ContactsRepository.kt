package com.example.myprofile.mycontacts

import com.github.javafaker.Faker

typealias UsersListener = (users: List<Contact>) -> Unit

class ContactsRepository {

    private var contacts = mutableListOf<Contact>()

    private val listeners = mutableListOf<UsersListener>()

    init {
        val faker = Faker.instance()
        AVATARS.shuffle()
        contacts = (1..10).map { Contact(
            id = it.toLong(),
            name = faker.name().name(),
            career = faker.job().field(),
            avatar = AVATARS[it % AVATARS.size]
        ) }.toMutableList()
    }

    fun deleteUser(user: Contact) {
        val indexToDelete = contacts.indexOfFirst { it.id == user.id }
        if (indexToDelete != -1) {
            contacts = ArrayList(contacts)
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

//    fun getUsers(): MutableList<Contact> {
//        val contacts = mutableListOf<Contact>()
//
//        // Generate contacts
//        for (i in 1..10) {
//            val avatar = getRandomAvatar()
//            val name = generateRandomName()
//            val career = generateRandomCareer()
//            val contact = Contact(avatar, name, career)
//            contacts.add(contact)
//        }
//
//        return contacts
//    }
//
//    private fun generateRandomName(): String {
//        val names = listOf("John", "Emma", "Michael", "Sophia", "James", "Olivia")
//        val surnames = listOf("Smith", "Johnson", "Brown", "Taylor", "Miller")
//        val randomName = names.random()
//        val randomSurname = surnames.random()
//        return "$randomName $randomSurname"
//    }
//
//    private fun generateRandomCareer(): String {
//        val careers = listOf("Engineer", "Teacher", "Doctor", "Designer", "Developer", "Writer")
//        return careers.random()
//    }
//
//    private fun getRandomAvatar() = AVATARS.random()
}