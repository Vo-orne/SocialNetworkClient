package com.example.myprofile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Data class representing a contact.
 * This class defines the structure of the "contacts" table in the Room database.
 *
 * @Entity annotation marks this class as a table in the database.
 *
 * @param avatar URL or path to the contact's avatar image.
 * @param name The name of the contact.
 * @param career The career or job title of the contact.
 * @param address The address of the contact.
 * @param id The unique ID of the contact, used as the primary key in the database.
 */
@Entity(tableName = "contacts")
data class Contact(
    val avatar: String? = null,
    var name: String? = null,
    var career: String? = null,
    var address: String? = null,
    @PrimaryKey val id: Long = 0
) : Serializable