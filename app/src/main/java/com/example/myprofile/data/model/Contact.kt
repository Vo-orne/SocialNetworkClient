package com.example.myprofile.data.model

import java.io.Serializable

/**
 * The Contact data class represents a single contact with its details.
 * @param id The unique identifier for the contact.
 * @param avatar The URL of the contact's avatar or profile picture.
 * @param name The name of the contact.
 * @param career The career or job field of the contact.
 * @param address The address of the contact.
 * Implements Serializable to allow instances of Contact to be serialized and passed
 * between different components or activities.
 */
data class Contact(
    val avatar: String? = null,
    var name: String? = null,
    var career: String? = null,
    var address: String? = null,
    val id: Long = 0
): Serializable

