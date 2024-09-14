package com.example.myprofile.presentation.ui.fragments.add_contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.database.interfaces.ContactDao
import com.example.myprofile.data.model.ContactsResponse
import com.example.myprofile.data.repository.UserDataRepository
import com.example.myprofile.data.model.UsersResponse
import com.example.myprofile.data.repository.repository_impl.UsersRepositoryImpl
import com.example.myprofile.domain.states.ApiState
import com.example.myprofile.presentation.utils.ext.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(
    private val usersRepositoryImpl: UsersRepositoryImpl,
    private val contactDao: ContactDao,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private var _contactsToAdd = MutableLiveData<List<Contact>>()
    val contactsToAdd: LiveData<List<Contact>> = _contactsToAdd

    private val _allUsersLiveData = MutableLiveData<ApiState>(ApiState.Initial)
    val allUsersLiveData: LiveData<ApiState> = _allUsersLiveData

    private val _contactLiveData = MutableLiveData<ApiState>(ApiState.Initial)

    private val _states: MutableLiveData<ArrayList<Pair<Long, ApiState>>> = MutableLiveData(ArrayList())
    val states: LiveData<ArrayList<Pair<Long, ApiState>>> = _states

    fun getAllUsers() = viewModelScope.launch(Dispatchers.Main) {
        _allUsersLiveData.value = ApiState.Loading

        val response = usersRepositoryImpl.getAllUsers( // Calls the repository to get the data
            userDataRepository.accessToken!!
        )

        saveUsers(response)
        _allUsersLiveData.value = response // Passes registration status to LiveData
    }

    /**
     * Method to save users
     */
    private fun saveUsers(response: ApiState) {
        if (response is ApiState.Success<*>) {
            val data = response.data as UsersResponse.Data

            // Uses postValue to update LiveData asynchronously
            _contactsToAdd.postValue(data.users?.map { it.toContact() } ?: emptyList())
        } else {
            log("Failed to get users: $response")
        }
    }

    fun addContact(contact: Contact) = viewModelScope.launch(Dispatchers.Main) {
        _contactLiveData.value = ApiState.Loading
        _states.value = arrayListOf(Pair(contact.id, ApiState.Loading))

        val response = usersRepositoryImpl.addContact(
            userDataRepository.currentUser!!.id,
            contact,
            userDataRepository.accessToken!!
        )
        addContactToRepository(response)
        _contactLiveData.value = response
        _states.value = arrayListOf(Pair(contact.id, response))
    }

    private fun addContactToRepository(response: ApiState) = viewModelScope.launch(Dispatchers.IO) {
        if (response is ApiState.Success<*>) {
            val data = response.data as ContactsResponse.Data
            val contact = data.contacts!!.map { it.toContact() }
            contactDao.insertContact(contact.first())  // Save the contact in the database
        }
    }
}