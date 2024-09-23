package com.example.myprofile.domain.di.api_service_module

import com.example.myprofile.domain.network.UserApiService
import com.example.myprofile.domain.network.UsersApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

/**
 * Dagger module for providing instances of API services.
 * This module provides the necessary API service instances to be used throughout the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {

    /**
     * Provides an instance of UserApiService.
     * This function creates an instance of UserApiService using Retrofit.
     *
     * @param retrofit The Retrofit instance used to create the UserApiService.
     * @return An instance of UserApiService.
     */
    @Provides
    fun provideApiService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    /**
     * Provides an instance of UsersApiService.
     * This function creates an instance of UsersApiService using Retrofit.
     *
     * @param retrofit The Retrofit instance used to create the UsersApiService.
     * @return An instance of UsersApiService.
     */
    @Provides
    fun providesUsersApiService(retrofit: Retrofit): UsersApiService {
        return retrofit.create(UsersApiService::class.java)
    }
}
