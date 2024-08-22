package com.example.myprofile.presentation.ui.fragments.contacts

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofile.data.database.Contact
import com.example.myprofile.data.database.ContactDao
import com.example.myprofile.data.model.ContactsResponse
import com.example.myprofile.data.model.UserDataRepository
import com.example.myprofile.data.repository.UsersRepositoryImpl
import com.example.myprofile.domain.ApiState
import com.example.myprofile.presentation.utils.Constants
import com.example.myprofile.presentation.utils.ext.isInternetAvailable
import com.example.myprofile.presentation.utils.ext.log
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val usersRepositoryImpl: UsersRepositoryImpl,
    private val userDataRepository: UserDataRepository,
    private val contactDao: ContactDao,
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _contacts =
        MutableLiveData<List<Contact>>() // Live data for saving the list of users
    val contacts: LiveData<List<Contact>> = _contacts // Public access to live data

    private val _isMultiselect = MutableLiveData(false) // Live data for saving the list of users
    val isMultiselect: LiveData<Boolean> = _isMultiselect // Public access to live data

    private val _contactsLiveData = MutableLiveData<ApiState>(ApiState.Initial)
    val contactsLiveData: LiveData<ApiState> = _contactsLiveData

    private val _deletionLiveData = MutableLiveData<ApiState>(ApiState.Initial)
    val deletionLiveData = _deletionLiveData

    private val _restoreContactLiveData = MutableLiveData<ApiState>(ApiState.Initial)
    val restoreContactLiveData = _restoreContactLiveData

    /**
     * A list of the last saved contacts that will be possible to return.
     */
    private var _lastDeletedContacts = mutableListOf<Contact>()
    var lastDeletedContacts = _lastDeletedContacts

    init {
        loadContacts()
    }

    /**
     * Receive a list of contacts from the local database (Room) or from the server,
     * if there is no data in the database.
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
                    _contacts.postValue(localContacts)  // Post contacts to UI
                }
            }
        }
    }


    private fun getUserContacts() = viewModelScope.launch(Dispatchers.IO) {
        _contactsLiveData.postValue(ApiState.Loading)

        val response = usersRepositoryImpl.getUserContacts(
            userDataRepository.currentUser!!.id,
            userDataRepository.accessToken!!
        )

        withContext(Dispatchers.Main) {
            saveUsers(response)  // Save the data to the database
            _contactsLiveData.value = response
        }
    }


    /**
     * Method for saving received users in the database.
     */
    private fun saveUsers(response: ApiState) = viewModelScope.launch(Dispatchers.IO) {
        if (response is ApiState.Success<*>) {
            val data = response.data as ContactsResponse.Data
            val contacts = data.contacts!!.map {
                it.toContact().also { contact -> log(contact) }
            }

            contactDao.deleteAllContacts()
            for (contact in contacts) {
                contactDao.insertContact(contact)
            }
            _contacts.postValue(contacts)
        }
    }



    /**
     * Removes a specific contact from the contact list.
     * @param user The contact to be deleted from the contact list.
     */
    fun deleteUser(user: Contact) {
        _lastDeletedContacts.clear()
        _lastDeletedContacts.add(user)
    }

    fun deleteSelectedUserContacts(selectedContacts: HashSet<Pair<Contact, Int>>) {
        _lastDeletedContacts.clear()
        for (contact in selectedContacts) {
            deleteUserContact(contact.first)
        }
        loadContacts()
    }

    fun deleteUserContact(contact: Contact) = viewModelScope.launch(Dispatchers.Main) {
        _deletionLiveData.postValue(ApiState.Loading)
        _lastDeletedContacts.add(contact)

        val response = usersRepositoryImpl.deleteUserContact(
            userDataRepository.currentUser!!.id,
            contact.id,
            userDataRepository.accessToken!!
        )

        if (response is ApiState.Success<*>) {
            contactDao.deleteContact(contact)  // Delete the contact from the database
        }

        loadContacts()
        _deletionLiveData.postValue(response)
    }

    /**
     * Returns the last deleted contacts that were deleted from the contact list back.
     */
    fun restoreLastDeletedContact() {
        for (contact in _lastDeletedContacts) {
            restoreContact(contact)
        }
        _lastDeletedContacts.clear()
    }

    private fun restoreContact(contact: Contact) = viewModelScope.launch(Dispatchers.Main) {
        _restoreContactLiveData.value = ApiState.Loading

        val response = usersRepositoryImpl.addContact(
            userDataRepository.currentUser!!.id,
            contact,
            userDataRepository.accessToken!!
        )
        loadContacts()
        _restoreContactLiveData.value = response
    }

    fun setMultiselect() {
        _isMultiselect.value = !_isMultiselect.value!!
    }

    fun notificationSearch(context: Context) {
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            notificationManager.notify(0, notificationBuilder.build())
        }
    }
}