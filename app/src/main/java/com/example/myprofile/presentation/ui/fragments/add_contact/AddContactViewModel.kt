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

/**
 * ViewModel of the AddContactFragment class, containing the processing of receiving
 * and transferring data to/from the server or database.
 *
 * @property usersRepositoryImpl Repository for user-related operations.
 * @property contactDao DAO for contact-related operations.
 * @property userDataRepository Repository for user data.
 */
@HiltViewModel
class AddContactViewModel @Inject constructor(
    private val usersRepositoryImpl: UsersRepositoryImpl,
    private val contactDao: ContactDao,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    // LiveData holding the list of contacts to add.
    private var _contactsToAdd = MutableLiveData<List<Contact>>()
    val contactsToAdd: LiveData<List<Contact>> = _contactsToAdd

    // LiveData holding the state of all users retrieval.
    private val _allUsersLiveData = MutableLiveData<ApiState>(ApiState.Initial)
    val allUsersLiveData: LiveData<ApiState> = _allUsersLiveData

    // LiveData holding the state of contact addition.
    private val _contactLiveData = MutableLiveData<ApiState>(ApiState.Initial)

    // LiveData holding the state of each contact addition process.
    private val _states: MutableLiveData<ArrayList<Pair<Long, ApiState>>> = MutableLiveData(ArrayList())
    val states: LiveData<ArrayList<Pair<Long, ApiState>>> = _states

    /**
     * Retrieves all users from the repository.
     */
    fun getAllUsers() = viewModelScope.launch(Dispatchers.Main) {
        _allUsersLiveData.value = ApiState.Loading

        // Calls the repository to get the list of all users.
        val response = usersRepositoryImpl.getAllUsers(
            userDataRepository.accessToken!!
        )

        // Processes and saves the retrieved users.
        saveUsers(response)
        _allUsersLiveData.value = response // Updates LiveData with the retrieved data.
    }

    /**
     * Processes and saves the list of users.
     *
     * @param response The response from the repository.
     */
    private fun saveUsers(response: ApiState) {
        if (response is ApiState.Success<*>) {
            val data = response.data as UsersResponse.Data

            // Updates LiveData with the list of contacts to add.
            _contactsToAdd.postValue(data.users?.map { it.toContact() } ?: emptyList())
        } else {
            log("Failed to get users: $response")
        }
    }

    /**
     * Adds a contact to the user's contact list.
     *
     * @param contact The contact to be added.
     */
    fun addContact(contact: Contact) = viewModelScope.launch(Dispatchers.Main) {
        _contactLiveData.value = ApiState.Loading
        _states.value = arrayListOf(Pair(contact.id, ApiState.Loading))

        // Calls the repository to add the contact.
        val response = usersRepositoryImpl.addContact(
            userDataRepository.currentUser!!.id,
            contact,
            userDataRepository.accessToken!!
        )
        // Processes and saves the added contact.
        addContactToRepository(response)
        _contactLiveData.value = response
        _states.value = arrayListOf(Pair(contact.id, response))
    }

    /**
     * Saves the added contact in the local database.
     *
     * @param response The response from the repository.
     */
    private fun addContactToRepository(response: ApiState) = viewModelScope.launch(Dispatchers.IO) {
        if (response is ApiState.Success<*>) {
            val data = response.data as ContactsResponse.Data
            val contact = data.contacts!!.map { it.toContact() }
            // Saves the first contact from the list to the database.
            contactDao.insertContact(contact.first())
        }
    }
}
