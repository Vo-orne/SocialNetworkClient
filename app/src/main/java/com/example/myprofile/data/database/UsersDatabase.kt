package com.example.myprofile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myprofile.data.database.interfaces.UserDao
import com.example.myprofile.data.model.Contact

/**
 * The Room database class for storing user-related data.
 * This class serves as the main access point to the persisted user data.
 *
 * @Database annotation defines the list of entities and the version of the database.
 */
@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {

    /**
     * Abstract method to get the UserDao.
     * Used to perform database operations on the user-related data.
     *
     * @return An instance of UserDao.
     */
    abstract fun userDao(): UserDao
}
