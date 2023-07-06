package com.example.myprofile

import android.app.Application
import com.example.myprofile.mycontacts.ContactsRepository

class App : Application() {

    val contactsRepository: ContactsRepository by lazy { ContactsRepository(this) }
}