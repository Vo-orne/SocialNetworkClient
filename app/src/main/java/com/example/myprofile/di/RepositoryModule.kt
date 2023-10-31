package com.example.myprofile.di

import android.content.Context
import com.example.myprofile.data.repository.ContactsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideContactRepository(
        @ApplicationContext context: Context
    ): ContactsRepository {
        return ContactsRepository(context)
    }
}