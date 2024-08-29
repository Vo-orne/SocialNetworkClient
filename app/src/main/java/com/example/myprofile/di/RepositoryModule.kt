package com.example.myprofile.di

import com.example.myprofile.data.repository.UserRepositoryImpl
import com.example.myprofile.data.repository.UsersRepositoryImpl
import com.example.myprofile.domain.ApiService
import com.example.myprofile.domain.UsersApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

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