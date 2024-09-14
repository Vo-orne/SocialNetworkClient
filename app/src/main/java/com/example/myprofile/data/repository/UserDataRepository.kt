package com.example.myprofile.data.repository

import com.example.myprofile.data.model.Contact
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor() {
    var currentUser: Contact? = null
    var accessToken: String? = null
    var refreshToken: String? = null
}