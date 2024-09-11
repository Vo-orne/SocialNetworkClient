package com.example.myprofile.data.model

import android.content.Context
import androidx.room.Room
import com.example.myprofile.data.database.ContactDao
import com.example.myprofile.data.database.ContactsDatabase
import com.example.myprofile.data.database.UserDao
import com.example.myprofile.data.database.UsersDatabase
import com.example.myprofile.presentation.utils.Constants.CONTACTS_DB
import com.example.myprofile.presentation.utils.Constants.USERS_DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideContactsDatabase(@ApplicationContext context: Context): ContactsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ContactsDatabase::class.java,
            CONTACTS_DB
        ).build()
    }

    @Provides
    @Singleton
    fun provideContactDao(appDatabase: ContactsDatabase): ContactDao {
        return appDatabase.contactDao()
    }

    @Provides
    @Singleton
    fun provideUsersDatabase(@ApplicationContext context: Context): UsersDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            UsersDatabase::class.java,
            USERS_DB
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(userDatabase: UsersDatabase): UserDao {
        return userDatabase.userDao()
    }
}
