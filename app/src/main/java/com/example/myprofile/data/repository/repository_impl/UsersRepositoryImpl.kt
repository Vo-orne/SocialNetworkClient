package com.example.myprofile.data.repository.repository_impl

import com.example.myprofile.data.model.Contact
import com.example.myprofile.domain.states.ApiState
import com.example.myprofile.domain.network.UsersApiService
import com.example.myprofile.presentation.utils.Constants.API_ERROR
import com.example.myprofile.presentation.utils.Constants.AUTHORIZATION_PREFIX
import javax.inject.Inject

/**
 * Implementation of the UsersRepository interface.
 * This class handles operations related to users by interacting with the UsersApiService.
 *
 * @param apiService An instance of UsersApiService for making network requests related to users.
 */
class UsersRepositoryImpl @Inject constructor(private val apiService: UsersApiService) {

    /**
     * Retrieves all users.
     * This method sends a request to the API to get a list of all users.
     *
     * @param accessToken The authorization token for making the request.
     * @return An ApiState indicating the result of the operation to retrieve users.
     */
    suspend fun getAllUsers(
        accessToken: String
    ): ApiState {
        return try {
            val response = apiService.getAllUsers(
                "$AUTHORIZATION_PREFIX $accessToken"
            )
            response.data?.let {
                ApiState.Success(it)
            } ?: ApiState.Error(response.message.toString())
        } catch (e: Exception) {
            ApiState.Error("$API_ERROR ${e.message.toString()}")
        }
    }

    /**
     * Adds a contact to a user.
     * This method sends a request to the API to add a contact to the specified user's contact list.
     *
     * @param userId The ID of the user to whom the contact will be added.
     * @param contact The contact to be added.
     * @param accessToken The authorization token for making the request.
     * @return An ApiState indicating the result of the operation to add the contact.
     */
    suspend fun addContact(userId: Long, contact: Contact, accessToken: String): ApiState {
        return try {
            val response =
                apiService.addContact(
                    userId,
                    "$AUTHORIZATION_PREFIX $accessToken",
                    contact.id
                )
            response.data?.let {
                ApiState.Success(it)
            } ?: ApiState.Error(response.message.toString())
        } catch (e: Exception) {
            ApiState.Error("$API_ERROR ${e.message.toString()}")
        }
    }

    /**
     * Retrieves a user's contacts.
     * This method sends a request to the API to get the list of contacts for a specific user.
     *
     * @param userId The ID of the user whose contacts will be retrieved.
     * @param accessToken The authorization token for making the request.
     * @return An ApiState indicating the result of the operation to retrieve the user's contacts.
     */
    suspend fun getUserContacts(userId: Long, accessToken: String): ApiState {
        return try {
            val response =
                apiService.getUserContacts(
                    userId,
                    "$AUTHORIZATION_PREFIX $accessToken"
                )
            response.data.let {
                ApiState.Success(it)
            } ?: ApiState.Error(response.message.toString())
        } catch (e: Exception) {
            ApiState.Error("$API_ERROR ${e.message.toString()}")
        }
    }

    /**
     * Deletes a contact from a user's contact list.
     * This method sends a request to the API to remove a specific contact from the user's contact list.
     *
     * @param userId The ID of the user from whose contact list the contact will be deleted.
     * @param contactId The ID of the contact to be deleted.
     * @param accessToken The authorization token for making the request.
     * @return An ApiState indicating the result of the operation to delete the contact.
     */
    suspend fun deleteUserContact(userId: Long, contactId: Long, accessToken: String): ApiState {
        return try {
            val response =
                apiService.deleteUserContact(
                    userId,
                    contactId,
                    "$AUTHORIZATION_PREFIX $accessToken"
                )
            response.data?.let {
                ApiState.Success(it)
            } ?: ApiState.Error(response.message.toString())
        } catch (e: Exception) {
            ApiState.Error("$API_ERROR ${e.message.toString()}")
        }
    }
}
