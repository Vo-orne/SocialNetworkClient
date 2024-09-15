package com.example.myprofile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myprofile.data.database.interfaces.ContactDao
import com.example.myprofile.data.model.Contact

/**
 * The Room database class for storing contacts.
 * This class serves as the main access point to the persisted data.
 *
 * @Database annotation defines the list of entities and the version of the database.
 */
@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class ContactsDatabase : RoomDatabase() {

    /**
     * Abstract method to get the ContactDao.
     * Used to perform database operations on the contacts table.
     *
     * @return An instance of ContactDao.
     */
    abstract fun contactDao(): ContactDao
}