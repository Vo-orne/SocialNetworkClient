package com.example.myprofile.data.database.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myprofile.data.model.Contact

/**
 * Data Access Object (DAO) for managing user data in the Room database.
 */
@Dao
interface UserDao {

    /**
     * Inserts a user into the database.
     * If a user with the same ID already exists, it replaces it.
     *
     * @param contact The user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(contact: Contact)

    /**
     * Retrieves a user from the database by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user with the specified ID or null if it does not exist.
     */
    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getUserById(id: Long): Contact?

    /**
     * Deletes a specific user from the database.
     *
     * @param contact The user to be deleted.
     */
    @Delete
    suspend fun deleteUser(contact: Contact)
}
