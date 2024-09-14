package com.example.myprofile.data.database.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myprofile.data.model.Contact

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(contact: Contact)

    @Query("SELECT * FROM contacts")
    suspend fun getAllUsers(): List<Contact>

    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getUserById(id: Long): Contact?

    @Delete
    suspend fun deleteUser(contact: Contact)
}