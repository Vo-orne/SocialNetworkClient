package com.example.myprofile.domain.network

import com.example.myprofile.data.model.UserResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

/**
 * Retrofit API service interface for user-related operations.
 * This interface defines the endpoints and request methods for managing users.
 */
interface UserApiService {

    /**
     * Method for registering a new user.
     *
     * @param email The email of the user.
     * @param password The password for the user account.
     * @param name The name of the user (optional).
     * @param phone The phone number of the user (optional).
     * @return A [UserResponse] containing the result of the registration.
     */
    @POST("users")
    suspend fun registerUser(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("name") name: String?,
        @Query("phone") phone: String?
    ): UserResponse

    /**
     * Method for logging in a user.
     *
     * @param email The email of the user.
     * @param password The password for the user account.
     * @return A [UserResponse] containing the result of the login.
     */
    @POST("login")
    suspend fun loginUser(
        @Query("email") email: String,
        @Query("password") password: String
    ): UserResponse

    /**
     * Method for editing an existing user.
     *
     * @param id The ID of the user to be edited.
     * @param accessToken The authorization token for the request.
     * @param name The new name of the user.
     * @param phone The new phone number of the user.
     * @param address The new address of the user (optional).
     * @param career The new career of the user (optional).
     * @param birthday The new birthday of the user (optional).
     * @return A [UserResponse] containing the result of the update.
     */
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
