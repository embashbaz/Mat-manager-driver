package com.example.matatumanageruser.services

import android.content.Intent
import androidx.lifecycle.LifecycleService
import com.example.matatumanageruser.utils.Constant

class TrackingService :  LifecycleService(){


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                Constant.ACTION_START_OR_RESUME_SERVICE ->{

                }

                Constant.ACTION_PAUSE_SERVICE -> {

                }

                Constant.ACTION_STOP_SERVICE -> {

                }

            }

        }


        return super.onStartCommand(intent, flags, startId)
    }
}