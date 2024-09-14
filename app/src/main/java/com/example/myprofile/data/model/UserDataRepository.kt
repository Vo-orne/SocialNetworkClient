package com.example.myprofile.data.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor() {
    var currentUser: Contact? = null
    var accessToken: String? = null
    var refreshToken: String? = null
}