package com.example.myprofile.presentation.ui.fragments.my_profile

import androidx.lifecycle.ViewModel
import com.example.myprofile.data.repository.ContactsRepository

class MyProfileViewModel(private val contactsRepository: ContactsRepository): ViewModel() {

    fun allowPhoneContacts() {
        contactsRepository.allowPhoneContacts()
    }
}