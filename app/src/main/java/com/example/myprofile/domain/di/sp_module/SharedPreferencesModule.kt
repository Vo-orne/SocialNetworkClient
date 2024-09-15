package com.example.myprofile.domain.di.sp_module

import android.content.Context
import android.content.SharedPreferences
import com.example.myprofile.presentation.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger module for providing SharedPreferences.
 * This module ensures that the SharedPreferences instance is provided.
 */
@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    /**
     * Provides an instance of SharedPreferences.
     * This function returns a SharedPreferences instance configured
     * to use a specific file name and private mode.
     *
     * @param context The context used to get the SharedPreferences instance.
     * @return An instance of SharedPreferences.
     */
    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            Constants.SHARED_PREFERENCES_KEY,
            Context.MODE_PRIVATE
        )
    }
}
