package com.example.myprofile.domain

import com.example.myprofile.data.model.UserResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("users")
    suspend fun registerUser(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("name") name: String?,
        @Query("phone") phone: String?
    ): UserResponse

    @POST("login")
    suspend fun loginUser(
        @Query("email") email: String,
        @Query("password") password: String
    ): UserResponse
}