package com.example.myprofile.presentation.ui.fragments.auth.sign_up

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.UserDataRepository
import com.example.myprofile.data.model.UserResponse
import com.example.myprofile.data.repository.UserRepositoryImpl
import com.example.myprofile.domain.ApiState
import com.example.myprofile.presentation.utils.Validation
import com.example.myprofile.presentation.utils.ext.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val userRepositoryImpl: UserRepositoryImpl
) : ViewModel() {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val phone = MutableLiveData<String>()

    private val _registerLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val registerLiveData: MutableLiveData<Boolean> = _registerLiveData

    fun isValidEmail() : Boolean = Validation.isValidEmail(email.value!!)

    /**
     * Private method to validate the entered password
     */
    fun isValidPassword(): String? = Validation.isValidPassword(password.value!!)

//    fun saveAutoLogin(email: String, password: String, editor: SharedPreferences.Editor) {
//        _registerLiveData.value = isValidEmail(email) && isValidPassword(password) == null
//        if(_registerLiveData.value == true) {
//            editor.putBoolean(Constants.AUTO_LOGIN_KEY, _registerLiveData.value!!)
//            editor.apply()
//        }
//    }
//--------------------------------------------------------------------------------------------------

    private val _registerStateFlow = MutableStateFlow<ApiState>(ApiState.Initial)
    val registerState: StateFlow<ApiState> = _registerStateFlow

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

        // Passes registration status to LiveData
        _registerStateFlow.value = response
    }

    /**
     * Method to save user data
     */
    private fun saveUserData(response: ApiState) {
        if (response is ApiState.Success<*>) {
            // Updates data in the userDataRepository upon successful registration
            val data = response.data as UserResponse.Data
            userDataRepository.currentUser = data.user
            userDataRepository.accessToken = data.accessToken
            userDataRepository.refreshToken = data.refreshToken
            log("" +
                    "currentUser = ${userDataRepository.currentUser}, " +
                    "accessToken = ${userDataRepository.accessToken}, " +
                    "refreshToken = $userDataRepository.refreshToken"
            )
        }
    }
}