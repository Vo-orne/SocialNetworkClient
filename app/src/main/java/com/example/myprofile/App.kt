package com.example.myprofile

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The App class represents the custom application class for the MyProfile app.
 * It extends the Application class and serves as the entry point for the application.
 * The App class is responsible for initializing global application-level components and services.
 */
@HiltAndroidApp
class App : Application() {}