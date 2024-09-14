package com.example.myprofile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myprofile.data.database.interfaces.UserDao
import com.example.myprofile.data.model.Contact

@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}