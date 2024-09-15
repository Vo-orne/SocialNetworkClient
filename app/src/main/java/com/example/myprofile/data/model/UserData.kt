package com.example.myprofile.data.model

import java.util.Date

/**
 * Data class representing user information.
 * A data class representing user information.
 * This class contains various details about a user that can be stored on the server.
 *
 * @param image URL or path to the user's profile image.
 * @param name The name of the user.
 * @param career The career or job title of the user.
 * @param address The address of the user.
 * @param id The unique ID of the user.
 * @param email The email address of the user.
 * @param phone The phone number of the user.
 * @param birthday The birth date of the user.
 * @param facebook URL to the user's Facebook profile.
 * @param instagram URL to the user's Instagram profile.
 * @param twitter URL to the user's Twitter profile.
 * @param linkedin URL to the user's LinkedIn profile.
 * @param createdAt The timestamp indicating when the user was created.
 * @param updatedAt The timestamp indicating when the user was last updated.
 */
data class UserData(
    val image: String? = null,
    val name: String? = null,
    val career: String? = null,
    val address: String? = null,
    val id: Long,
    val email: String,
    val phone: String? = null,
    val birthday: Date? = null,
    val facebook: String? = null,
    val instagram: String? = null,
    val twitter: String? = null,
    val linkedin: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
) {
    /**
     * Converts the UserData object to a Contact object.
     * This method extracts the relevant fields to create a Contact instance.
     *
     * @return A Contact instance with the user's image, name, career, address, and ID.
     */
    fun toContact(): Contact = Contact(image, name, career, address, id)
}
