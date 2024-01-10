package com.example.myprofile.presentation.ui.fragments.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.UserDataRepository
import com.example.myprofile.data.repository.UserRepositoryImpl
import com.example.myprofile.domain.ApiState
import com.example.myprofile.presentation.utils.ext.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _userStateFlow = MutableLiveData<ApiState>(ApiState.Initial)
    val userStateFlow: LiveData<ApiState> = _userStateFlow

    private lateinit var userName: String
    private var userAddress: String? = null
    private var userCareer: String? = null

    fun editUser(
        name: String,
        phone: String,
        address: String? = null,
        career: String?,
        birthday: Date? = null
    ) = viewModelScope.launch(Dispatchers.Main) {
        _userStateFlow.value = ApiState.Loading

        val response = userRepositoryImpl.editUser(
            userDataRepository.currentUser!!.id,
            userDataRepository.accessToken!!,
            name, phone,
            address, career,
            birthday
        )
        log("response = $response")
        userName = name
        userAddress = address
        userCareer = career
        _userStateFlow.value = response

    }

    fun setUserData() {
        userDataRepository.currentUser!!.name = userName
        userDataRepository.currentUser!!.address = userAddress
        userDataRepository.currentUser!!.career = userCareer
    }
}