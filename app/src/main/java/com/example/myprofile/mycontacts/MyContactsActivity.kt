package com.example.myprofile.mycontacts

import android.Manifest.permission.READ_CONTACTS
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.Constants
import com.example.myprofile.R
import com.example.myprofile.databinding.ActivityMyContactsBinding
import com.example.myprofile.main.MainActivity

class MyContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyContactsBinding
    private lateinit var contactsViewModel: ContactsViewModel
    private lateinit var recyclerViewContacts: RecyclerView
    private lateinit var contactsAdapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactsViewModel = ViewModelProvider(this)[ContactsViewModel::class.java]
        binding = ActivityMyContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Configure the watcher to update the user interface when data changes
        contactsViewModel.contactsList.observe(this) { contactsList ->
            contactsAdapter.contacts = contactsList as MutableList<Contact> // Update adapter data
            contactsAdapter.notifyDataSetChanged() // Notify the adapter about changes
        }

        requestContactsPermission()

        setListeners()
    }

    private fun setupRecyclerView() {
        recyclerViewContacts = binding.recyclerViewContacts
        recyclerViewContacts.layoutManager = LinearLayoutManager(this)
        contactsAdapter = ContactsAdapter(contactsViewModel.contactsList.value?.toMutableList() ?: mutableListOf())
        recyclerViewContacts.adapter = contactsAdapter
    }

    private fun setListeners() {
        binding.buttonMyContactsBack.setOnClickListener {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }
        }
    }

    private fun requestContactsPermission() {
        if (ContextCompat.checkSelfPermission(this, READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(READ_CONTACTS), Constants.CONTACTS_PERMISSION_CODE)
        } else {
            contactsViewModel.loadContacts(getContactsPhoneBook())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.CONTACTS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                contactsViewModel.loadContacts(getContactsPhoneBook())
            } else {
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                }
            }
        }
    }

    private fun getContactsPhoneBook(): MutableList<Contact> {
        val contentResolver: ContentResolver = this.contentResolver
        val contactsUri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection: Array<String> = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI
        )

        val sortOrder = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"

        val cursor: Cursor? = contentResolver.query(contactsUri, projection, null, null, sortOrder)

        val result = mutableListOf<Contact>()
        cursor?.use {
            val nameColumnIndex: Int = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val photoColumnIndex: Int = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)

            while (it.moveToNext()) {
                val name: String = it.getString(nameColumnIndex)
                val photoUriString: String? = it.getString(photoColumnIndex)
                val photoUri: Uri? = photoUriString?.let { uriString ->
                    Uri.parse(uriString)
                }

                val contact = if (photoUri != null) {
                    Contact(photoUri.toString(), name, "")
                } else {
                    Contact("", name, "")
                }
                addContact(contact)
                result.add(contact)
            }
        }
        return result
    }

    private fun addContact(contact: Contact) {
        contactsAdapter.contacts.add(contact)
        contactsAdapter.notifyItemInserted(contactsAdapter.contacts.size - 1)
    }
}