package com.example.myprofile.data.repository

import com.example.myprofile.domain.ApiService
import com.example.myprofile.domain.ApiState
import com.example.myprofile.presentation.utils.Constants.API_ERROR
import com.example.myprofile.presentation.utils.Constants.AUTHORIZATION_PREFIX
import java.util.Date
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val apiService: ApiService) {

    suspend fun registerUser(
        email: String,
        password: String,
        name: String?,
        phone: String?
    ): ApiState {
        return try {
            val response = apiService.registerUser(email, password, name, phone)
            response.data?.let {
                ApiState.Success(it)
            } ?: ApiState.Error(response.message.toString())
        } catch (e: Exception) {
            ApiState.Error("$API_ERROR ${e.message.toString()}")
        }
    }

    suspend fun loginUser(
        email: String,
        password: String
    ): ApiState {
        return try {
            val response = apiService.loginUser(email, password)
            response.data?.let {
                ApiState.Success(it)
            } ?: ApiState.Error(response.message.toString())
        } catch (e: Exception) {
            ApiState.Error("$API_ERROR ${e.message.toString()}")
        }
    }

    suspend fun editUser(
        id: Long, accessToken: String,
        name: String, phone: String,
        address: String?, career: String?,
        birthday: Date?
    ): ApiState {
        return try {
            val response = apiService.editUser(
                id,
                "$AUTHORIZATION_PREFIX $accessToken",
                name, phone,
                address, career,
                birthday
            )
            response.data?.let {
                ApiState.Success(it)
            } ?: ApiState.Error(response.message.toString())
        } catch (e: Exception) {
            ApiState.Error("$API_ERROR ${e.message.toString()}")
        }
    }
}