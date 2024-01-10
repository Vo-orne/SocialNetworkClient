package com.example.myprofile.presentation.ui.fragments.my_profile

import androidx.lifecycle.ViewModel
import com.example.myprofile.data.model.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    fun getUserName(): String {
        return userDataRepository.currentUser?.name.toString()
    }

    fun getUserCareer(): String {
        return userDataRepository.currentUser?.career.toString()
    }

    fun getUserAddress(): String {
        return userDataRepository.currentUser?.address.toString()
    }
}