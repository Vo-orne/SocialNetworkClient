package com.example.myprofile.mycontacts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        contactsViewModel.contactsList.observe(this, Observer { contactsList ->
            Log.d("myTag", "Contacts list size: ${contactsList.size}")
            contactsAdapter.contacts = contactsList as MutableList<Contact> // Update adapter data
            contactsAdapter.notifyDataSetChanged() // Notify the adapter about changes
        })

        // Download the list of users
        contactsViewModel.loadContacts()

        //addExampleContact()
        setListeners()
    }

    private fun setupRecyclerView() {
        recyclerViewContacts = binding.recyclerViewContacts
        recyclerViewContacts.layoutManager = LinearLayoutManager(this)
        contactsAdapter = ContactsAdapter(contactsViewModel.contactsList.value?.toMutableList() ?: mutableListOf())
        recyclerViewContacts.adapter = contactsAdapter
    }

//    private fun addExampleContact() {
//        val exampleContact = Contact(getString(R.), getString(R.string.name_surname), getString(R.string.career))
//        addContact(exampleContact)
//    }

    private fun addContact(contact: Contact) {
        contactsAdapter.contacts.add(contact)
        contactsAdapter.notifyItemInserted(contactsAdapter.contacts.size - 1)
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
}