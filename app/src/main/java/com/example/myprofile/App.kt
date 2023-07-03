package com.example.myprofile

import android.app.Application
import android.content.ContentResolver
import com.example.myprofile.mycontacts.ContactsRepository

class App : Application() {

    val contactsRepository = ContactsRepository()
}