package com.example.myprofile.domain

import com.example.myprofile.data.model.UserResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

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

    @FormUrlEncoded
    @PUT("users/{userId}")
    suspend fun editUser(
        @Path("userId") id: Long,
        @Header("Authorization") accessToken: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("address") address: String?,
        @Field("career") career: String?,
        @Field("birthday") birthday: Date?
    ): UserResponse
}