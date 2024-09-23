package com.example.myprofile.presentation.ui.fragments.my_profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.myprofile.data.repository.UserDataRepository
import com.example.myprofile.presentation.utils.ext.removeAutoLoginData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel of the MyProfileFragment class, for managing user profile data.
 */
@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val sP: SharedPreferences
) : ViewModel() {

    private val editor by lazy { sP.edit() }

    /**
     * Returns the username using the userDataRepository.
     */
    fun getUserName(): String {
        return userDataRepository.currentUser?.name.toString()
    }

    /**
     * Returns the career of the user using the userDataRepository.
     */
    fun getUserCareer(): String {
        return userDataRepository.currentUser?.career.toString()
    }

    /**
     * Returns the address of the user using the userDataRepository.
     */
    fun getUserAddress(): String {
        return userDataRepository.currentUser?.address.toString()
    }

    /**
     * Clears user data and auto-login data:
     *
     * Uses editor.removeAutoLoginData() to remove auto-login data from SharedPreferences.
     * Clears user data, access token, and refresh token in userDataRepository.
     */
    fun clearUserData() {
        editor.removeAutoLoginData()
        with(userDataRepository) {
            currentUser = null
            accessToken = null
            refreshToken = null
        }
    }
}
