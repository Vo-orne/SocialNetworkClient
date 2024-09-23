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
import com.example.myprofile.data.model.Contact
import com.example.myprofile.data.database.interfaces.ContactDao
import com.example.myprofile.data.model.ContactsResponse
import com.example.myprofile.data.repository.UserDataRepository
import com.example.myprofile.data.repository.repository_impl.UsersRepositoryImpl
import com.example.myprofile.domain.states.ApiState
import com.example.myprofile.presentation.utils.Constants
import com.example.myprofile.presentation.utils.ext.isInternetAvailable
import com.example.myprofile.presentation.utils.ext.log
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel of the MyContactsFragment class.
 * Manages contact data and their operations.
 * It is responsible for downloading, saving, deleting and restoring contacts.
 */
@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val usersRepositoryImpl: UsersRepositoryImpl,
    private val userDataRepository: UserDataRepository,
    private val contactDao: ContactDao,
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat,
    @ApplicationContext private val context: Context
) : ViewModel() {

    /**
     * LiveData to store your contact list. External access to data is provided through contacts.
     */
    private val _contacts = MutableLiveData<List<Contact>>()
    /**
     * LiveData to store your contact list. External access to data is provided through contacts.
     */
    val contacts: LiveData<List<Contact>> = _contacts

    /**
     * LiveData for multi-select mode. External access is provided via isMultiselect.
     */
    private val _isMultiselect = MutableLiveData(false)
    /**
     * LiveData for multi-select mode. External access is provided via isMultiselect.
     */
    val isMultiselect: LiveData<Boolean> = _isMultiselect

    /**
     * LiveData to track API status (download, success, error).
     */
    private val _contactsLiveData = MutableLiveData<ApiState>(ApiState.Initial)
    /**
     * LiveData to track API status (download, success, error).
     */
    val contactsLiveData: LiveData<ApiState> = _contactsLiveData

    /**
     * LiveData to track contact deletion status.
     */
    private val _deletionLiveData = MutableLiveData<ApiState>(ApiState.Initial)

    /**
     * LiveData to track contact recovery status.
     */
    private val _restoreContactLiveData = MutableLiveData<ApiState>(ApiState.Initial)

    /**
     * List of recently deleted contacts that can be recovered.
     */
    private var _lastDeletedContacts = mutableListOf<Contact>()

    init {
        loadContacts()
    }

    /**
     * Downloads contacts from the local database (Room)
     * or from the server if there is no data in the database.
     * Checks the availability of the Internet.
     */
    private fun loadContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            if (context.isInternetAvailable()) {  // Check if internet is available
                getUserContacts()  // Fetch contacts from the server
            } else {
                val localContacts = contactDao.getAllContacts()  // Fetch contacts from Room
                if (localContacts.isEmpty()) {
                    _contactsLiveData.postValue(ApiState.Error(Constants.BD_EMPTY))
                } else {
                    _contacts.postValue(localContacts)  // Post contacts to UI
                }
            }
        }
    }

    /**
     * Retrieves the user's contacts from the server and stores them in the database.
     */
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
     * Stores received contacts in the database. Deletes all old contacts before inserting new ones.
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
     * Updates the last deleted user.
     * @param user The contact that will be the last deleted.
     */
    fun updateLastDeletedUser(user: Contact) {
        _lastDeletedContacts.clear()
        _lastDeletedContacts.add(user)
    }

    /**
     * Deletes the selected contacts. Updates the list of contacts after deletion.
     */
    fun deleteSelectedUserContacts(selectedContacts: HashSet<Pair<Contact, Int>>) {
        _lastDeletedContacts.clear()
        for (contact in selectedContacts) {
            deleteUserContact(contact.first)
        }
        loadContacts()
    }

    /**
     * Deletes a contact from the database and server. Updates the list of contacts after deletion.
     */
    fun deleteUserContact(contact: Contact) = viewModelScope.launch(Dispatchers.Main) {
        _deletionLiveData.postValue(ApiState.Loading)

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
     * Restore the last deleted contacts to the contact list.
     */
    fun restoreLastDeletedContacts() {
        for (contact in _lastDeletedContacts) {
            restoreContact(contact)
        }
        _lastDeletedContacts.clear()
    }

    /**
     * Restores a specific contact. Updates the contact list after recovery.
     */
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

    /**
     * Toggles multi-select mode.
     */
    fun setMultiselect() {
        _isMultiselect.value = !_isMultiselect.value!!
    }

    /**
     * Shows the community with search results if the user has granted permission to share.
     */
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
