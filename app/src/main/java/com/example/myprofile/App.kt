package com.example.myprofile

import android.app.Application
import com.example.myprofile.data.ContactsRepository

/**
 * The App class represents the custom application class for the MyProfile app.
 * It extends the Application class and serves as the entry point for the application.
 * The App class is responsible for initializing global application-level components and services.
 */
class App : Application() {

    /**
     * The ContactsRepository instance is created lazily using the application context.
     * It provides access to the contacts data and operations within the app.
     */
    val contactsRepository: ContactsRepository by lazy { ContactsRepository(this) }
}