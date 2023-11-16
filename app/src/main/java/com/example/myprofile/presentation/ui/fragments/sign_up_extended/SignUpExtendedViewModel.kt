package com.example.myprofile.presentation.ui.fragments.sign_up_extended

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.repository.UserRepositoryImpl
import com.example.myprofile.domain.ApiState
import com.example.myprofile.presentation.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpExtendedViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl
) : ViewModel() {

    private val _registerStateFlow = MutableStateFlow<ApiState>(ApiState.Initial)
    val registerState: StateFlow<ApiState> = _registerStateFlow

    fun registerUser(
        sharedPreferences: SharedPreferences,
        name: String,
        phone: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        _registerStateFlow.value = ApiState.Loading
            val email = sharedPreferences.getString(Constants.REGISTRATION_EMAIL, "")!!
            val password = sharedPreferences.getString(Constants.REGISTRATION_PASSWORD, "")!!

            _registerStateFlow.value = userRepositoryImpl.registerUser(
                email,
                password,
                name,
                phone
            )
        }

    /**
     * Method to save registration data
     */
    fun saveRegistrationData(
        name: String,
        accessToken: String,
        refreshToken: String,
        editor: SharedPreferences.Editor
    ) {
        with(editor) {
            putString(Constants.USER_NAME_KEY, name)
            putString(Constants.ACCESS_TOKEN, accessToken)
            putString(Constants.REFRESH_TOKEN, refreshToken)
            apply()
        }
    }
}