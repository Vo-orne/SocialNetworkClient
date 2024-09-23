package com.example.myprofile.presentation.ui.fragments.log_in

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.repository.UserDataRepository
import com.example.myprofile.data.model.UserResponse
import com.example.myprofile.data.repository.repository_impl.UserRepositoryImpl
import com.example.myprofile.domain.states.ApiState
import com.example.myprofile.presentation.utils.ext.getUserEmail
import com.example.myprofile.presentation.utils.ext.getUserPassword
import com.example.myprofile.presentation.utils.ext.saveAutoLoginData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel of the LogInFragment class. Processes user login and stores data for automatic login.
 */
@HiltViewModel
class LogInViewModel @Inject constructor(
    private val sP: SharedPreferences,  // SharedPreferences for storing login data
    private val userDataRepository: UserDataRepository,  // Repository for user data
    private val userRepositoryImpl: UserRepositoryImpl  // Repository for user login operations
) : ViewModel() {

    /**
     * MutableLiveData to store the user's entered email.
     */
    val email = MutableLiveData<String>()

    /**
     * MutableLiveData to store the user's entered password.
     */
    val password = MutableLiveData<String>()

    /**
     * Editor for SharedPreferences used to save data.
     */
    private val editor by lazy { sP.edit() }

    /**
     * Private MutableStateFlow to track login status (Loading, Success, Error, Initial).
     */
    private val _loginStateFlow = MutableStateFlow<ApiState>(ApiState.Initial)  // StateFlow for login state

    /**
     * MutableStateFlow to track login status (Loading, Success, Error, Initial).
     */
    val loginState: StateFlow<ApiState> = _loginStateFlow

    /**
     * Performs an asynchronous request for the user's login, updates the login state,
     * and stores the user's data after a successful login.
     */
    fun loginUser() = viewModelScope.launch(Dispatchers.IO) {
        _loginStateFlow.value = ApiState.Loading

        // Call the repository to perform login
        val response = userRepositoryImpl.loginUser(
            email.value!!,
            password.value!!
        )

        // Save user data upon successful login
        saveUserData(response)

        // Update login state
        _loginStateFlow.value = response
    }

    /**
     * Stores user data (after successful login) in the userDataRepository.
     */
    private fun saveUserData(response: ApiState) {
        if (response is ApiState.Success<*>) {
            val data = response.data as UserResponse.Data
            with(userDataRepository) {
                currentUser = data.user
                accessToken = data.accessToken
                refreshToken = data.refreshToken
            }
        }
    }

    /**
     * Stores data for automatic login in SharedPreferences.
     */
    fun saveAutoLoginData() {
        editor.saveAutoLoginData(email.value, password.value)
    }

    /**
     * Checks for auto-login saved data and calls the loginUser method to login.
     */
    fun autoLogin() {
        if (sP.getUserEmail().isNotEmpty() && sP.getUserPassword().isNotEmpty()) {
            email.value = sP.getUserEmail()
            password.value = sP.getUserPassword()
            loginUser()
        }
    }
}