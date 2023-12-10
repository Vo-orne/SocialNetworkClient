package com.example.myprofile.presentation.ui.fragments.add_contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.model.UserDataRepository
import com.example.myprofile.data.model.UsersResponse
import com.example.myprofile.data.repository.ContactsRepository
import com.example.myprofile.data.repository.UsersRepositoryImpl
import com.example.myprofile.domain.ApiState
import com.example.myprofile.presentation.utils.ext.UsersListener
import com.example.myprofile.presentation.utils.ext.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
    private val usersRepositoryImpl: UsersRepositoryImpl,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private var _contactsToAdd = MutableLiveData<List<Contact>>()
    val contactsToAdd: LiveData<List<Contact>> = _contactsToAdd

    private val selectedContacts = mutableListOf<Contact>()

    private val listener: UsersListener = {
        _contactsToAdd.value = it
    }

    private val _getAllUsersStateFlow = MutableStateFlow<ApiState>(ApiState.Initial)
    val getAllUsersStateFlow: StateFlow<ApiState> = _getAllUsersStateFlow

    fun getAllUsers() = viewModelScope.launch(Dispatchers.IO) {
        _getAllUsersStateFlow.value = ApiState.Loading

        // Calls the repository to get the data
        val response = usersRepositoryImpl.getAllUsers(
            userDataRepository.accessToken!!
        )

        saveUsers(response)

        // Passes registration status to LiveData
        _getAllUsersStateFlow.value = response
    }

    /**
     * Method to save users
     */
    private fun saveUsers(response: ApiState) {
        if (response is ApiState.Success<*>) {
            val data = response.data as UsersResponse.Data
            // Uses postValue to update LiveData asynchronously
            _contactsToAdd.postValue(data.users?.map { it.toContact() } ?: emptyList())
        }
    }

    fun selectContact(contact: Contact) {
        selectedContacts.add(contact)
    }

    fun addContacts() {
        if (selectedContacts.isNotEmpty()) {
            for(contact in selectedContacts) {
                contactsRepository.addContact(contact)
            }
        }
    }
}