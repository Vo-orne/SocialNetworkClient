package com.example.myprofile.data.model

/**
 * Data class representing the response structure for a request involving multiple users.
 * This class models the JSON response received from a network or server call regarding a list of users.
 *
 * @param status The status of the response, indicating success or failure.
 * @param code The response code, which can provide more information about the result of the request.
 * @param message An optional message providing additional details about the response.
 * @param data The data payload of the response, containing a list of users.
 */
data class UsersResponse(
    val status: String = "",
    val code: Int = 0,
    val message: String? = "",
    val data: Data? = null
) {
    /**
     * Nested data class representing the data portion of the users response.
     *
     * @param users A list of UserData objects representing multiple users.
     */
    data class Data(val users: List<UserData>?)
}
