package com.example.myprofile.presentation.ui.fragments.log_in

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.UserDataRepository
import com.example.myprofile.data.model.UserResponse
import com.example.myprofile.data.repository.UserRepositoryImpl
import com.example.myprofile.domain.ApiState
import com.example.myprofile.presentation.utils.ext.getUserEmail
import com.example.myprofile.presentation.utils.ext.getUserPassword
import com.example.myprofile.presentation.utils.ext.saveAutoLoginData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val sP: SharedPreferences,
    private val userDataRepository: UserDataRepository,
    private val userRepositoryImpl: UserRepositoryImpl
    ) : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val editor by lazy { sP.edit() }

    private val _loginStateFlow = MutableStateFlow<ApiState>(ApiState.Initial)
    val loginState: StateFlow<ApiState> = _loginStateFlow

    fun loginUser() = viewModelScope.launch(Dispatchers.IO) {
        _loginStateFlow.value = ApiState.Loading

        // Calls the repository to get the data
        val response = userRepositoryImpl.loginUser(
            email.value!!,
            password.value!!
        )

        saveUserData(response)

        // Passes authorization status to LiveData
        _loginStateFlow.value = response
    }

    /**
     * Method to save user data
     */
    private fun saveUserData(response: ApiState) {
        if (response is ApiState.Success<*>) {
            // Updates data in the userDataRepository upon successful authorization
            val data = response.data as UserResponse.Data
            with(userDataRepository) {
                currentUser = data.user
                accessToken = data.accessToken
                refreshToken = data.refreshToken
            }
        }
    }

    fun saveAutoLoginData() {
        editor.saveAutoLoginData(email.value, password.value)
    }


    fun autoLogin() {
        if (sP.getUserEmail().isNotEmpty() && sP.getUserPassword().isNotEmpty()) {
            email.value = sP.getUserEmail()
            password.value = sP.getUserPassword()
            loginUser()
        }
    }
}