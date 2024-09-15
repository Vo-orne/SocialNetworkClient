package com.example.myprofile.domain.di.db_module

import android.content.Context
import androidx.room.Room
import com.example.myprofile.data.database.ContactsDatabase
import com.example.myprofile.data.database.UsersDatabase
import com.example.myprofile.data.database.interfaces.ContactDao
import com.example.myprofile.data.database.interfaces.UserDao
import com.example.myprofile.presentation.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger module for providing instances of Room databases and DAOs.
 * This module ensures that only one instance of each database
 * and DAO is created throughout the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides an instance of ContactsDatabase.
     * This function creates an instance of ContactsDatabase using Room.
     *
     * @param context The application context used to create the ContactsDatabase.
     * @return An instance of ContactsDatabase.
     */
    @Provides
    @Singleton
    fun provideContactsDatabase(@ApplicationContext context: Context): ContactsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ContactsDatabase::class.java,
            Constants.CONTACTS_DB
        ).build()
    }

    /**
     * Provides an instance of ContactDao.
     * This function retrieves the ContactDao from the ContactsDatabase instance.
     *
     * @param appDatabase The ContactsDatabase instance from which to get the ContactDao.
     * @return An instance of ContactDao.
     */
    @Provides
    @Singleton
    fun provideContactDao(appDatabase: ContactsDatabase): ContactDao {
        return appDatabase.contactDao()
    }

    /**
     * Provides an instance of UsersDatabase.
     * This function creates an instance of UsersDatabase using Room.
     *
     * @param context The application context used to create the UsersDatabase.
     * @return An instance of UsersDatabase.
     */
    @Provides
    @Singleton
    fun provideUsersDatabase(@ApplicationContext context: Context): UsersDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            UsersDatabase::class.java,
            Constants.USERS_DB
        ).build()
    }

    /**
     * Provides an instance of UserDao.
     * This function retrieves the UserDao from the UsersDatabase instance.
     *
     * @param userDatabase The UsersDatabase instance from which to get the UserDao.
     * @return An instance of UserDao.
     */
    @Provides
    @Singleton
    fun provideUserDao(userDatabase: UsersDatabase): UserDao {
        return userDatabase.userDao()
    }
}