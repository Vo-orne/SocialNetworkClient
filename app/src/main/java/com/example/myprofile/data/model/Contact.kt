package com.example.myprofile.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.util.UUID

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
@Parcelize
data class Contact( // TODO: you can set default values
    val avatar: String,
    val name: String,
    val career: String,
    val address: String,
    val id: UUID = UUID.randomUUID()
): Parcelable

