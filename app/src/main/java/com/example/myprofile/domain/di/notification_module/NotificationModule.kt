package com.example.myprofile.domain.di.notification_module

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.myprofile.R
import com.example.myprofile.presentation.ui.activity.MainActivity
import com.example.myprofile.presentation.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger module for providing notification-related components.
 * This module ensures that notification components such as NotificationCompat.Builder
 * and NotificationManagerCompat are provided.
 */
@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    /**
     * Provides a NotificationCompat.Builder instance.
     * This function creates and configures a NotificationCompat.Builder
     * with the necessary settings for notifications.
     *
     * @param context The application context used to create the NotificationCompat.Builder.
     * @return An instance of NotificationCompat.Builder.
     */
    @Singleton
    @Provides
    fun createNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        // Create an intent that will be fired when the notification is clicked
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            data = Uri.parse("")
        }

        // Create a PendingIntent for the notification
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Build and return the NotificationCompat.Builder
        return NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(ContextCompat.getString(context, R.string.app_name))
            .setContentText(ContextCompat.getString(context, R.string.notification))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(0, ContextCompat.getString(context, R.string.search), pendingIntent)
            .setContentIntent(pendingIntent)
    }

    /**
     * Provides a NotificationManagerCompat instance.
     * This function creates and configures a NotificationManagerCompat instance
     * and registers a notification channel if necessary.
     *
     * @param context The application context used to create the NotificationManagerCompat.
     * @return An instance of NotificationManagerCompat.
     */
    @Singleton
    @Provides
    fun createNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat {
        val notificationManager = NotificationManagerCompat.from(context)
        // Create and register a notification channel for devices running Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.CHANNEL_ID,
                Constants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }
}
