package com.example.myprofile.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getString
import com.example.myprofile.R
import com.example.myprofile.presentation.ui.activity.MainActivity
import com.example.myprofile.presentation.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NotificationModule {

    @Singleton
    @Provides
    fun createNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
//        val ACTION_SNOOZE = "snooze"
//
//        val snoozeIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
//            action = ACTION_SNOOZE
//            putExtra(EXTRA_NOTIFICATION_ID, 0)
//        }
//        val snoozePendingIntent: PendingIntent =
//            PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)
//        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.notification_icon)
//            .setContentTitle("My notification")
//            .setContentText("Hello World!")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setContentIntent(pendingIntent)
//            .addAction(R.drawable.ic_snooze, getString(R.string.snooze),
//                snoozePendingIntent)
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(context, R.string.app_name))
            .setContentText(getString(context, R.string.notification))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(0, getString(context, R.string.search), pendingIntent)
            .setContentIntent(pendingIntent)
    }

    @Singleton
    @Provides
    fun createNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat {
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            // Register the channel with the system.
            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }
}