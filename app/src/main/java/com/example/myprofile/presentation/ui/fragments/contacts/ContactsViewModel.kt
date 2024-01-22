package com.example.myprofile.presentation.ui.fragments.contacts

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
import com.example.myprofile.presentation.utils.ext.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
    private val usersRepositoryImpl: UsersRepositoryImpl,
    private val userDataRepository: UserDataRepository
    ): ViewModel() {

    private val _contacts = MutableLiveData<List<Contact>>() // Live data for saving the list of users
    val contacts: LiveData<List<Contact>> = _contacts // Public access to live data

    private val _isMultiselect = MutableLiveData(false) // Live data for saving the list of users
    val isMultiselect: LiveData<Boolean> = _isMultiselect // Public access to live data

    private val _contactsLiveData = MutableLiveData<ApiState>(ApiState.Initial)
    val contactsLiveData: LiveData<ApiState> = _contactsLiveData

    private val _deletionLiveData = MutableLiveData<ApiState>(ApiState.Initial)
    val deletionLiveData = _deletionLiveData

    private val _restoreContactLiveData = MutableLiveData<ApiState>(ApiState.Initial)
    val restoreContactLiveData = _restoreContactLiveData

    private val listener: UsersListener = {
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
        log("I using getUserContacts()")
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

//    /**
//     * Deletes all selected contacts in multiselect mode.
//     * @param selectedContacts List of selected contacts transferred fragment.
//     */
//    fun deleteSelectedContacts(selectedContacts: HashSet<Pair<Contact, Int>>) {
//        contactsRepository.deleteSelectedContacts(selectedContacts)
//    }

    /**
     * Removes a specific contact from the contact list.
     * @param user The contact to be deleted from the contact list.
     * @param position The position of the contact to be removed from the contact list.
     */
    fun deleteUser(user: Contact, position: Int) {
        contactsRepository.clearLastDeletedContact()
        contactsRepository.deleteContact(user, position)
    }

    fun deleteSelectedUserContacts(selectedContacts: HashSet<Pair<Contact, Int>>) {
        for (contact in selectedContacts) {
            deleteUserContact(contact.first)
        }
    }

    fun deleteUserContact(contact: Contact) = viewModelScope.launch(Dispatchers.Main) {
        _deletionLiveData.value = ApiState.Loading

        val response = usersRepositoryImpl.deleteUserContact(
            userDataRepository.currentUser!!.id,
            contact.id,
            userDataRepository.accessToken!!
        )
        log("response = $response")
        _deletionLiveData.value = response
    }

    /**
     * Returns the last deleted contacts that were deleted from the contact list back.
     */
    fun restoreLastDeletedContact() {
        val lastDeletedContacts = contactsRepository.lastDeletedContacts

        for (contact in lastDeletedContacts) {
            restoreContact(contact)
        }
        contactsRepository.clearLastDeletedContact()
    }

    private fun restoreContact(contact: Contact) = viewModelScope.launch(Dispatchers.Main) {
        _restoreContactLiveData.value = ApiState.Loading

        val response = usersRepositoryImpl.addContact(
            userDataRepository.currentUser!!.id,
            contact,
            userDataRepository.accessToken!!
        )
        contactsRepository.addContact(contact)
        log("addContact = $response")
        _restoreContactLiveData.value = response
    }

    fun setMultiselect() {
        _isMultiselect.value = !_isMultiselect.value!!
    }
}