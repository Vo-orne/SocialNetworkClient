package com.example.myprofile.data.database.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myprofile.data.model.Contact

/**
 * Data Access Object (DAO) for managing contact data in the Room database.
 */
@Dao
interface ContactDao {

    /**
     * Inserts a contact into the database.
     * If a contact with the same ID already exists, it replaces it.
     *
     * @param contact The contact to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)

    /**
     * Retrieves all contacts from the database.
     *
     * @return A list of all contacts.
     */
    @Query("SELECT * FROM contacts")
    suspend fun getAllContacts(): List<Contact>

    /**
     * Retrieves a contact from the database by its ID.
     *
     * @param id The ID of the contact to retrieve.
     * @return The contact with the specified ID or null if it does not exist.
     */
    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Long): Contact?

    /**
     * Deletes a specific contact from the database.
     *
     * @param contact The contact to be deleted.
     */
    @Delete
    suspend fun deleteContact(contact: Contact)

    /**
     * Deletes all contacts from the database.
     */
    @Query("DELETE FROM contacts")
    suspend fun deleteAllContacts()
}
