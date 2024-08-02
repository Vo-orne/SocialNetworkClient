package com.example.myprofile.presentation.ui.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.model.ContactsResponse
import com.example.myprofile.data.model.UserDataRepository
import com.example.myprofile.data.repository.ContactsRepository
import com.example.myprofile.data.repository.UsersRepositoryImpl
import com.example.myprofile.domain.ApiState
import com.example.myprofile.presentation.utils.ext.UsersListener
import com.example.myprofile.presentation.utils.ext.filterContacts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
    private val usersRepositoryImpl: UsersRepositoryImpl,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _contacts = MutableLiveData<List<Contact>>() // Live data for saving the list of users
    val contacts: LiveData<List<Contact>> = _contacts // Public access to live data

    private val _allContacts = mutableListOf<Contact>() // To store the original list of contacts

    private val _contactsLiveData = MutableLiveData<ApiState>(ApiState.Initial)
    val contactsLiveData: LiveData<ApiState> = _contactsLiveData

    private val listener: UsersListener = {
        _allContacts.clear()
        _allContacts.addAll(it)
        _contacts.value = it
    }

    init {
        loadContacts()
    }

    /**
     * Removes all listeners after the program is finished.
     */
    override fun onCleared() {
        super.onCleared()
        contactsRepository.removeListener(listener)
    }

    /**
     * Getting a list of contacts.
     */
    private fun loadContacts() {
        getUserContacts()
        contactsRepository.addListener(listener)
    }

    private fun getUserContacts() = viewModelScope.launch(Dispatchers.Main) {
        _contactsLiveData.value = ApiState.Loading

        // Calls the repository to get the data
        val response = usersRepositoryImpl.getUserContacts(
            userDataRepository.currentUser!!.id,
            userDataRepository.accessToken!!
        )

        withContext(Dispatchers.Main) {
            saveUsers(response)
            _contactsLiveData.value = response
        }
    }

    private fun saveUsers(response: ApiState) {
        if (response is ApiState.Success<*>) {
            val data = response.data as ContactsResponse.Data
            val contacts = data.contacts!!.map { it.toContact() }
            for (contact in contacts) {
                contactsRepository.addContact(contact)
            }
        }
    }

    fun searchContacts(query: String) {
        val filteredContacts = _allContacts.filterContacts(query)
        _contacts.value = filteredContacts
    }
}