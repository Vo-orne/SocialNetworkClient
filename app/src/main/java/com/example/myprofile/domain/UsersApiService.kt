package com.example.myprofile.domain

import com.example.myprofile.data.model.ContactsResponse
import com.example.myprofile.data.model.UsersResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsersApiService {

    @GET("users")
    suspend fun getAllUsers(
        @Header("Authorization") accessToken: String
    ): UsersResponse

    @FormUrlEncoded
    @PUT("users/{userId}/contacts")
    suspend fun addContact(
        @Path("userId") userId: Long,
        @Header("Authorization") accessToken: String,
        @Field("contactId") contactId: Long
    ): ContactsResponse

    @GET("users/{userId}/contacts")
    suspend fun getUserContacts(
        @Path("userId") userId: Long,
        @Header("Authorization") accessToken: String
    ): ContactsResponse

    @DELETE("users/{userId}/contacts/{contactId}")
    suspend fun deleteUserContact(
        @Path("userId") userId: Long,
        @Path("contactId") contactId: Long,
        @Header("Authorization") accessToken: String,
    ): UsersResponse
}