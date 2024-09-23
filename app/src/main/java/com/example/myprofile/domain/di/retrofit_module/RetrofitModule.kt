package com.example.myprofile.domain.di.retrofit_module

import com.example.myprofile.presentation.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Dagger module for providing the Retrofit instance.
 * This module ensures that only one instance of Retrofit is created
 * and provided throughout the application.
 */
@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    /**
     * Provides an instance of Retrofit.
     * This function builds and provides a Retrofit instance configured
     * with the base URL and Gson converter.
     *
     * @param okHttpClient The OkHttpClient instance used by Retrofit.
     * @return An instance of Retrofit.
     */
    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
