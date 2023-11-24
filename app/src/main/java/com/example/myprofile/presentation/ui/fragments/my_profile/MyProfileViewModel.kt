package com.example.myprofile.presentation.ui.fragments.my_profile

import androidx.lifecycle.ViewModel
import com.example.myprofile.data.model.UserDataRepository
import com.example.myprofile.data.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    fun allowPhoneContacts() {
        contactsRepository.allowPhoneContacts()
    }

    fun getUserName(): String {
        return userDataRepository.currentUser?.name.toString()
    }
}