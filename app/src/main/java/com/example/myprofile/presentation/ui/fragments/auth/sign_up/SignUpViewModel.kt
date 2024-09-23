package com.example.myprofile.presentation.ui.fragments.auth.sign_up

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.repository.UserDataRepository
import com.example.myprofile.data.model.UserResponse
import com.example.myprofile.data.repository.repository_impl.UserRepositoryImpl
import com.example.myprofile.domain.states.ApiState
import com.example.myprofile.presentation.utils.Validation
import com.example.myprofile.presentation.utils.ext.saveAutoLoginData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This class is responsible for managing the user registration logic in the application. It uses LiveData and StateFlow to monitor data and registration status.
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val sP: SharedPreferences,
    private val userDataRepository: UserDataRepository,
    private val userRepositoryImpl: UserRepositoryImpl
) : ViewModel() {

    // LiveData objects for user input
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val phone = MutableLiveData<String>()

    // Flag to check if the checkbox is checked
    var isCheckBoxChecked: Boolean = false
    private val editor by lazy { sP.edit() }

    /**
     * Method to validate the entered email
     */
    fun isValidEmail() : Boolean = Validation.isValidEmail(email.value!!)

    /**
     * Method to validate the entered password
     */
    fun isValidPassword(): String? = Validation.isValidPassword(password.value!!)

    // StateFlow to observe registration status
    private val _registerStateFlow = MutableStateFlow<ApiState>(ApiState.Initial)
    val registerState: StateFlow<ApiState> = _registerStateFlow

    /**
     * Method to register a user by calling the repository
     */
    fun registerUser() = viewModelScope.launch(Dispatchers.IO) {
        _registerStateFlow.value = ApiState.Loading

        // Calls the repository to get the data
        val response = userRepositoryImpl.registerUser(
            email.value!!,
            password.value!!,
            name.value!!,
            phone.value!!
        )

        saveUserData(response)

        // Passes registration status to StateFlow
        _registerStateFlow.value = response
    }

    /**
     * Method to save user data in the repository
     */
    private fun saveUserData(response: ApiState) {
        if (response is ApiState.Success<*>) {
            // Updates data in the userDataRepository upon successful registration
            val data = response.data as UserResponse.Data
            userDataRepository.currentUser = data.user
            userDataRepository.accessToken = data.accessToken
            userDataRepository.refreshToken = data.refreshToken
        }
    }

    /**
     * Method to save auto-login data in SharedPreferences
     */
    fun saveAutoLoginData() {
        editor.saveAutoLoginData(email.value, password.value)
    }
}
