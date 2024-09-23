package com.example.myprofile.presentation.ui.fragments.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.repository.UserDataRepository
import com.example.myprofile.data.repository.repository_impl.UserRepositoryImpl
import com.example.myprofile.domain.states.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel of the EditProfileFragment class.
 * Responsible for processing user profile edits.
 * It interacts with repositories to update user data and maintains API state.
 */
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    /**
     * LiveData to observe the API state during user profile editing.
     */
    private val _userStateFlow = MutableLiveData<ApiState>(ApiState.Initial)
    /**
     * LiveData to observe the API state during user profile editing.
     */
    val userStateFlow: LiveData<ApiState> = _userStateFlow

    // User profile data fields
    private lateinit var userName: String
    private var userAddress: String? = null
    private var userCareer: String? = null

    /**
     * Called to edit the user profile.
     * It sends a request to the server through the repository
     * and updates local variables with data from the server's response.
     *
     * @param name User's name
     * @param phone User's phone number
     * @param address User's address (optional)
     * @param career User's career (optional)
     * @param birthday User's birthday (optional)
     */
    fun editUser(
        name: String,
        phone: String,
        address: String? = null,
        career: String?,
        birthday: Date? = null
    ) = viewModelScope.launch(Dispatchers.Main) {
        _userStateFlow.value = ApiState.Loading

        // Make a network call to edit user data
        val response = userRepositoryImpl.editUser(
            userDataRepository.currentUser!!.id,
            userDataRepository.accessToken!!,
            name, phone,
            address, career,
            birthday
        )

        // Update local variables with the new data
        userName = name
        userAddress = address
        userCareer = career

        // Post the result of the API call to LiveData
        _userStateFlow.value = response
    }

    /**
     * Updates the user's data in the repository after successfully editing the profile.
     * Sets new values for name, address, and career.
     */
    fun setUserData() {
        userDataRepository.currentUser!!.name = userName
        userDataRepository.currentUser!!.address = userAddress
        userDataRepository.currentUser!!.career = userCareer
    }
}
