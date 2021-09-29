package com.example.matatumanageruser.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.matatumanageruser.MainActivity
import com.example.matatumanageruser.utils.Constant

class TrackingService :  LifecycleService(){

    var serviceIsStarted = true

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                Constant.ACTION_START_OR_RESUME_SERVICE ->{
                    if (serviceIsStarted){
                        startForegroundService()
                        serviceIsStarted = false
                    }
                }

                Constant.ACTION_PAUSE_SERVICE -> {

                }

                Constant.ACTION_STOP_SERVICE -> {

                }

            }

        }


        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, Constant.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentTitle("Matatu Manager")
            .setContentText("Matatu in service")
            .setContentIntent(getPendingIntent())

        startForeground(Constant.NOTIFICATION_ID, notificationBuilder.build())

    }

    private fun getPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also { it.action = Constant.SHOW_TRACKING_FRAGMENT },
        FLAG_UPDATE_CURRENT

    )


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){

        val channel = NotificationChannel(
            Constant.NOTIFICATION_CHANNEL_ID,
            Constant.NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )

        notificationManager.createNotificationChannel(channel)

    }
}