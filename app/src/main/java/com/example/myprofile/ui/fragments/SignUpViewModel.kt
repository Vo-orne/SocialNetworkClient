package com.example.myprofile.ui.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {
    private val _registerLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val registerLiveData: MutableLiveData<Boolean> = _registerLiveData

    fun validationInputs(email: String, password: String) {
        _registerLiveData.value = true
    }
}