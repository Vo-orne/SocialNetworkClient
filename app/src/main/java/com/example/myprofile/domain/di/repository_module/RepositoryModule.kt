package com.example.myprofile.domain.di.repository_module

import com.example.myprofile.data.repository.repository_impl.UserRepositoryImpl
import com.example.myprofile.data.repository.repository_impl.UsersRepositoryImpl
import com.example.myprofile.domain.network.ApiService
import com.example.myprofile.domain.network.UsersApiService
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