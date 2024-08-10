package com.example.myprofile.data.model

import com.example.myprofile.data.database.Contact
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor() {
    var currentUser: Contact? = null
    var accessToken: String? = null
    var refreshToken: String? = null
}