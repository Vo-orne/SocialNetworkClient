package com.example.myprofile.data.repository

import com.example.myprofile.domain.ApiService
import com.example.myprofile.domain.ApiState
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
            ApiState.Error("ApiState.Error = ${e.message.toString()}")
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
            ApiState.Error("ApiState.Error = ${e.message.toString()}")
        }
    }
}