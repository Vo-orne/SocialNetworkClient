package com.example.myprofile.data.repository

import com.example.myprofile.data.model.Contact
import com.example.myprofile.domain.ApiState
import com.example.myprofile.domain.UsersApiService
import com.example.myprofile.presentation.utils.Constants
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(private val apiService: UsersApiService) {

    suspend fun getAllUsers(
        accessToken: String
    ): ApiState {
        return try {
            val response = apiService.getAllUsers(
                "${Constants.AUTHORIZATION_PREFIX} $accessToken"
            )
            response.data?.let {
                ApiState.Success(it)
            } ?: ApiState.Error(response.message.toString())
        } catch (e: Exception) {
            ApiState.Error("ApiState.Error = ${e.message.toString()}")
        }
    }

    suspend fun addContact(userId: Long, contact: Contact, accessToken: String): ApiState {
        return try {
            val response =
                apiService.addContact(
                    userId,
                    "${Constants.AUTHORIZATION_PREFIX} $accessToken",
                    contact.id
                )
            response.data.let {
                ApiState.Success(it)
            }
        } catch (e: Exception) {
            ApiState.Error("ApiState.Error = ${e.message.toString()}")
        }
    }

    suspend fun getUserContacts(userId: Long, accessToken: String): ApiState {
        return try {
            val response =
                apiService.getUserContacts(
                    userId,
                    "${Constants.AUTHORIZATION_PREFIX} $accessToken"
                )
            response.data.let {
                ApiState.Success(it)
            }
        } catch (e: Exception) {
            ApiState.Error("ApiState.Error = ${e.message.toString()}")
        }
    }

    suspend fun deleteUserContact(userId: Long, contactId: Long, accessToken: String): ApiState {
        return try {
            val response =
                apiService.deleteUserContact(
                    userId,
                    contactId,
                    "${Constants.AUTHORIZATION_PREFIX} $accessToken"
                )
            response.data.let {
                ApiState.Success(it)
            }
        } catch (e: Exception) {
            ApiState.Error("ApiState.Error = ${e.message.toString()}")
        }
    }
}