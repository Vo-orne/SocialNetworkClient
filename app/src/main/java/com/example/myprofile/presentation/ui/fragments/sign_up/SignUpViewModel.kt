package com.example.myprofile.presentation.ui.fragments.sign_up

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myprofile.presentation.utils.Constants
import com.example.myprofile.presentation.utils.Parser
import com.example.myprofile.presentation.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {
    private val _registerLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val registerLiveData: MutableLiveData<Boolean> = _registerLiveData

    fun isValidEmail(email: String) : Boolean = Validation.isValidEmail(email)

    /**
     * Private method to validate the entered password
     */
    fun isValidPassword(password: String): String? = Validation.isValidPassword(password)

    fun saveAutoLogin(email: String, password: String, editor: SharedPreferences.Editor) {
        _registerLiveData.value = isValidEmail(email) && isValidPassword(password) == null
        if(_registerLiveData.value == true) {
            editor.putBoolean(Constants.AUTO_LOGIN_KEY, _registerLiveData.value!!)
            editor.apply()
        }
    }

    /**
     * Method to save the user's name from the entered email
     */
    fun saveUserName(email: String, editor: SharedPreferences.Editor) {
        val userName: String = Parser.parsEmail(email)
        editor.putString(Constants.USER_NAME_KEY, userName)
        editor.apply()
    }
}