package com.example.myprofile.presentation.ui.fragments.my_profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.myprofile.data.model.UserDataRepository
import com.example.myprofile.presentation.utils.ext.removeAutoLoginData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val sP: SharedPreferences
) : ViewModel() {

    private val editor by lazy { sP.edit() }

    fun getUserName(): String {
        return userDataRepository.currentUser?.name.toString()
    }

    fun getUserCareer(): String {
        return userDataRepository.currentUser?.career.toString()
    }

    fun getUserAddress(): String {
        return userDataRepository.currentUser?.address.toString()
    }

    fun clearUserData() {
        editor.removeAutoLoginData()
        with(userDataRepository) {
            currentUser = null
            accessToken = null
            refreshToken = null
        }
    }
}