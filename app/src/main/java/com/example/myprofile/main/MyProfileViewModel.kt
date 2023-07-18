package com.example.myprofile.main

import androidx.lifecycle.ViewModel
import com.example.myprofile.mycontacts.ContactsRepository

class MyProfileViewModel(private val contactsRepository: ContactsRepository): ViewModel() {

    fun allowPhoneContacts() {
        contactsRepository.allowPhoneContacts()
    }
}