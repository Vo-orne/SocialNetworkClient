package com.example.myprofile.domain

import com.example.myprofile.data.model.UsersResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface UsersApiService {

    @GET("users")
    suspend fun getAllUsers(
        @Header("Authorization") accessToken: String
    ): UsersResponse
}