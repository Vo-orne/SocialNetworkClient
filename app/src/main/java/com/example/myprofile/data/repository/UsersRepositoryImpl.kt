package com.example.myprofile.data.repository

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
}