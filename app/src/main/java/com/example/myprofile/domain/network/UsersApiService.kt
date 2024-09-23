package com.example.myprofile.domain.network

import com.example.myprofile.data.model.ContactsResponse
import com.example.myprofile.data.model.UsersResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Retrofit API service interface for user contacts management.
 * This interface defines the endpoints and request methods for handling user contacts.
 */
interface UsersApiService {

    /**
     * Method for retrieving all users.
     *
     * @param accessToken The authorization token for the request.
     * @return A [UsersResponse] containing the list of all users.
     */
    @GET("users")
    suspend fun getAllUsers(
        @Header("Authorization") accessToken: String
    ): UsersResponse

    /**
     * Method for adding a contact to a user's list.
     *
     * @param userId The ID of the user to whom the contact will be added.
     * @param accessToken The authorization token for the request.
     * @param contactId The ID of the contact to be added.
     * @return A [ContactsResponse] containing the result of the add operation.
     */
    @FormUrlEncoded
    @PUT("users/{userId}/contacts")
    suspend fun addContact(
        @Path("userId") userId: Long,
        @Header("Authorization") accessToken: String,
        @Field("contactId") contactId: Long
    ): ContactsResponse

    /**
     * Method for retrieving all contacts for a specific user.
     *
     * @param userId The ID of the user whose contacts are to be retrieved.
     * @param accessToken The authorization token for the request.
     * @return A [ContactsResponse] containing the list of contacts for the user.
     */
    @GET("users/{userId}/contacts")
    suspend fun getUserContacts(
        @Path("userId") userId: Long,
        @Header("Authorization") accessToken: String
    ): ContactsResponse

    /**
     * Method for deleting a contact from a user's list.
     *
     * @param userId The ID of the user from whose contacts the contact will be removed.
     * @param contactId The ID of the contact to be deleted.
     * @param accessToken The authorization token for the request.
     * @return A [UsersResponse] containing the result of the delete operation.
     */
    @DELETE("users/{userId}/contacts/{contactId}")
    suspend fun deleteUserContact(
        @Path("userId") userId: Long,
        @Path("contactId") contactId: Long,
        @Header("Authorization") accessToken: String,
    ): UsersResponse
}
