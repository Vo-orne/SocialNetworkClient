package com.example.myprofile.data.repository.repository_impl

import com.example.myprofile.domain.network.UserApiService
import com.example.myprofile.domain.states.ApiState
import com.example.myprofile.presentation.utils.Constants
import java.util.Date
import javax.inject.Inject

/**
 * Implementation of the UserRepository interface.
 * This class handles user-related operations by interacting with the UserApiService.
 *
 * @param userApiService An instance of UserApiService for making network requests.
 */
class UserRepositoryImpl @Inject constructor(private val userApiService: UserApiService) {

    /**
     * Registers a new user.
     * This method sends a request to the API to register a user with the provided details.
     *
     * @param email The email address of the user.
     * @param password The password for the user account.
     * @param name The optional name of the user.
     * @param phone The optional phone number of the user.
     * @return An ApiState indicating the result of the registration operation.
     */
    suspend fun registerUser(
        email: String,
        password: String,
        name: String?,
        phone: String?
    ): ApiState {
        return try {
            val response = userApiService.registerUser(email, password, name, phone)
            response.data?.let {
                ApiState.Success(it)
            } ?: ApiState.Error(response.message.toString())
        } catch (e: Exception) {
            ApiState.Error("${Constants.API_ERROR} ${e.message.toString()}")
        }
    }

    /**
     * Logs in a user.
     * This method sends a request to the API to authenticate a user with the provided email and password.
     *
     * @param email The email address of the user.
     * @param password The password for the user account.
     * @return An ApiState indicating the result of the login operation.
     */
    suspend fun loginUser(
        email: String,
        password: String
    ): ApiState {
        return try {
            val response = userApiService.loginUser(email, password)
            response.data?.let {
                ApiState.Success(it)
            } ?: ApiState.Error(response.message.toString())
        } catch (e: Exception) {
            ApiState.Error("${Constants.API_ERROR} ${e.message.toString()}")
        }
    }

    /**
     * Edits user information.
     * This method sends a request to the API to update the user's details.
     *
     * @param id The unique ID of the user to be edited.
     * @param accessToken The authorization token for making the request.
     * @param name The new name of the user.
     * @param phone The new phone number of the user.
     * @param address The optional new address of the user.
     * @param career The optional new career of the user.
     * @param birthday The optional new birthday of the user.
     * @return An ApiState indicating the result of the edit operation.
     */
    suspend fun editUser(
        id: Long, accessToken: String,
        name: String, phone: String,
        address: String?, career: String?,
        birthday: Date?
    ): ApiState {
        return try {
            val response = userApiService.editUser(
                id,
                "${Constants.AUTHORIZATION_PREFIX} $accessToken",
                name, phone,
                address, career,
                birthday
            )
            response.data?.let {
                ApiState.Success(it)
            } ?: ApiState.Error(response.message.toString())
        } catch (e: Exception) {
            ApiState.Error("${Constants.API_ERROR} ${e.message.toString()}")
        }
    }
}