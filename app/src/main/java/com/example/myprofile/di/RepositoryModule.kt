package com.example.myprofile.di

import android.content.Context
import com.example.myprofile.data.repository.ContactsRepository
import com.example.myprofile.data.repository.UserRepositoryImpl
import com.example.myprofile.data.repository.UsersRepositoryImpl
import com.example.myprofile.domain.ApiService
import com.example.myprofile.domain.UsersApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideContactRepository(
        @ApplicationContext context: Context
    ): ContactsRepository {
        return ContactsRepository(context)
    }

    @Provides
    fun providesRepositoryImpl(
        apiService: ApiService
    ): UserRepositoryImpl {
        return UserRepositoryImpl(apiService)
    }

    @Provides
    fun providesUsersRepositoryImpl(
        usersApiService: UsersApiService
    ): UsersRepositoryImpl {
        return UsersRepositoryImpl(usersApiService)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }
}