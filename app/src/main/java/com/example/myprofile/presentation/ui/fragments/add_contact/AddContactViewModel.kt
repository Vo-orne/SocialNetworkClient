package com.example.myprofile.presentation.ui.fragments.add_contact

import androidx.lifecycle.ViewModel
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository
    ): ViewModel() {

    fun addContact(contact: Contact) {
        contactsRepository.addContact(contact)
    }
}