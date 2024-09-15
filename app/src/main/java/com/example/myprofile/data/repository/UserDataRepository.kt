package com.example.myprofile.data.repository

import com.example.myprofile.data.model.Contact
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class for storing user data.
 * This class maintains the current user information and tokens used for authentication.
 * It is annotated with @Singleton to ensure that only one instance of this class is created.
 */
@Singleton
class UserDataRepository @Inject constructor() {
    /**
     * The current user object.
     * This holds information about the currently logged-in user.
     */
    var currentUser: Contact? = null

    /**
     * The access token for authentication.
     * This token is used to authorize API requests.
     */
    var accessToken: String? = null

    /**
     * The refresh token for authentication.
     * This token is used to obtain a new access token when the current one expires.
     */
    var refreshToken: String? = null
}