package com.example.myprofile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.databinding.ActivityMyContactsBinding

class MyContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyContactsBinding
    private lateinit var recyclerViewContacts: RecyclerView
    private lateinit var contactsAdapter: ContactsAdapter
    private val contactsList = mutableListOf<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        addExampleContact()
        setListeners()
    }

    private fun setupRecyclerView() {
        recyclerViewContacts = binding.recyclerViewContacts
        contactsAdapter = ContactsAdapter(contactsList)
        recyclerViewContacts.adapter = contactsAdapter
        recyclerViewContacts.layoutManager = LinearLayoutManager(this)
    }

    private fun addExampleContact() {
        val exampleContact = Contact("John Doe", "Developer")
        addContact(exampleContact)
    }

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

