package com.example.myprofile.domain.di.repository_module

import com.example.myprofile.data.repository.repository_impl.UserRepositoryImpl
import com.example.myprofile.data.repository.repository_impl.UsersRepositoryImpl
import com.example.myprofile.domain.network.UserApiService
import com.example.myprofile.domain.network.UsersApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Dagger module for providing repository implementations and OkHttpClient.
 * This module ensures that the necessary repositories and HTTP client instances are provided.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provides an instance of UserRepositoryImpl.
     * This function returns an implementation of UserRepositoryImpl
     * that uses UserApiService for network operations.
     *
     * @param userApiService The UserApiService used by the UserRepositoryImpl.
     * @return An instance of UserRepositoryImpl.
     */
    @Provides
    fun providesRepositoryImpl(
        userApiService: UserApiService
    ): UserRepositoryImpl {
        return UserRepositoryImpl(userApiService)
    }

    /**
     * Provides an instance of UsersRepositoryImpl.
     * This function returns an implementation of UsersRepositoryImpl
     * that uses UsersApiService for network operations.
     *
     * @param usersApiService The UsersApiService used by the UsersRepositoryImpl.
     * @return An instance of UsersRepositoryImpl.
     */
    @Provides
    fun providesUsersRepositoryImpl(
        usersApiService: UsersApiService
    ): UsersRepositoryImpl {
        return UsersRepositoryImpl(usersApiService)
    }

    /**
     * Provides an instance of OkHttpClient.
     * This function returns a new instance of OkHttpClient configured with default settings.
     *
     * @return An instance of OkHttpClient.
     */
    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }
}
