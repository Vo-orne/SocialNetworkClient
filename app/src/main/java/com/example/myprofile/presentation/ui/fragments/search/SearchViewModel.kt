package com.example.myprofile.presentation.ui.fragments.search

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.database.interfaces.ContactDao
import com.example.myprofile.data.model.ContactsResponse
import com.example.myprofile.data.repository.UserDataRepository
import com.example.myprofile.data.repository.repository_impl.UsersRepositoryImpl
import com.example.myprofile.domain.states.ApiState
import com.example.myprofile.presentation.utils.Constants
import com.example.myprofile.presentation.utils.ext.filterContacts
import com.example.myprofile.presentation.utils.ext.isInternetAvailable
import com.example.myprofile.presentation.utils.ext.log
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel of the SearchFragment class, for managing search operations and fetching contacts.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val usersRepositoryImpl: UsersRepositoryImpl,
    private val contactDao: ContactDao,
    private val userDataRepository: UserDataRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    /**
     * Private LiveData for saving and observing the list of contacts.
     */
    private val _contacts = MutableLiveData<List<Contact>>()
    /**
     * LiveData for saving and observing the list of contacts.
     */
    val contacts: LiveData<List<Contact>> = _contacts

    /**
     * Private list to save the original list of contacts for filtering.
     */
    private val _allContacts = mutableListOf<Contact>()

    /**
     * Private LiveData to save API request state.
     */
    private val _contactsLiveData = MutableLiveData<ApiState>(ApiState.Initial)

    /**
     * Initializes the loading of contacts when the ViewModel is created.
     */
    init {
        loadContacts()
    }

    /**
     * Loads contacts either from the server or local database.
     */
    private fun loadContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            if (context.isInternetAvailable()) {  // Check if internet is available
                log(Constants.INTERNET)
                getUserContacts()  // Fetch contacts from the server
            } else {
                log(Constants.NO_INTERNET)
                val localContacts = contactDao.getAllContacts()  // Fetch contacts from Room

                if (localContacts.isEmpty()) {
                    _contactsLiveData.postValue(ApiState.Error(Constants.BD_EMPTY))
                } else {
                    _allContacts.clear()
                    _allContacts.addAll(localContacts)
                    _contacts.postValue(localContacts)  // Post contacts to UI
                }
            }
        }
    }

    /**
     * Fetches user contacts from the server.
     */
    private fun getUserContacts() = viewModelScope.launch(Dispatchers.IO) {
        _contactsLiveData.postValue(ApiState.Loading)

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

    /**
     * Saves fetched user contacts into the local database and updates LiveData.
     */
    private fun saveUsers(response: ApiState) = viewModelScope.launch(Dispatchers.IO) {
        if (response is ApiState.Success<*>) {
            val data = response.data as ContactsResponse.Data
            val contacts = data.contacts!!.map { it.toContact() }

            // Updating _allContacts
            _allContacts.clear()
            _allContacts.addAll(contacts)

            for (contact in contacts) {
                contactDao.insertContact(contact)  // Store contacts in the database
            }
            _contacts.postValue(contacts)  // Updating live data for the UI
        }
    }

    /**
     * Searches contacts based on the query string.
     */
    fun searchContacts(query: String) {
        val filteredContacts = _allContacts.filterContacts(query)
        _contacts.value = filteredContacts
    }
}
