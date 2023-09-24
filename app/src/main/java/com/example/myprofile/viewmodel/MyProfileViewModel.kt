package com.example.myprofile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myprofile.data.repository.ContactsRepository

class MyProfileViewModel(private val contactsRepository: ContactsRepository): ViewModel() {

    fun allowPhoneContacts() {
        contactsRepository.allowPhoneContacts()
    }
}