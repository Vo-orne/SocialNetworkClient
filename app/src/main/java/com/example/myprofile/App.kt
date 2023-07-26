package com.example.myprofile

import android.app.Application
import com.example.myprofile.data.ContactsRepository

class App : Application() {

    val contactsRepository: ContactsRepository by lazy { ContactsRepository(this) }
}