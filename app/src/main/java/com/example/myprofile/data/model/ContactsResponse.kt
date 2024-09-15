package com.example.myprofile.data.model

/**
 * Data class representing the response structure for contacts.
 * This class models the JSON response received from a network or server call.
 *
 * @param status The status of the response, indicating success or failure.
 * @param code The response code, which can provide more information about the response.
 * @param message An optional message providing additional details about the response.
 * @param data The data payload of the response, containing a list of contacts.
 */
data class ContactsResponse(
    val status: String,
    val code: String,
    val message: String?,
    val data: Data
) {
    /**
     * Nested data class representing the data portion of the response.
     *
     * @param contacts A list of UserData objects representing the contacts.
     */
    data class Data(val contacts: List<UserData>?)
}
