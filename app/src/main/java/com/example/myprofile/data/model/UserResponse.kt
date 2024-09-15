package com.example.myprofile.data.model

/**
 * Data class representing the response structure for a user-related request.
 * This class models the JSON response received from a network or server call regarding user data.
 *
 * @param status The status of the response, indicating success or failure.
 * @param code The response code, which can provide more information about the result of the request.
 * @param message An optional message providing additional details about the response.
 * @param data The data payload of the response, containing user information and tokens.
 */
data class UserResponse(
    val status: String = "",
    val code: Int = 0,
    val message: String? = "",
    val data: Data? = null
) {
    /**
     * Nested data class representing the data portion of the user response.
     *
     * @param user An instance of Contact representing the user's data.
     * @param accessToken The access token for authenticating the user.
     * @param refreshToken The refresh token for renewing the user's access.
     */
    data class Data(
        val user: Contact,
        val accessToken: String,
        val refreshToken: String
    )
}
