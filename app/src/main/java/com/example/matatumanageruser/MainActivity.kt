package com.example.matatumanageruser

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.example.matatumanageruser.utils.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigateToTripFragment(intent)
    }

    @SuppressLint("ResourceType")
    private fun navigateToTripFragment(intent: Intent?){
        if(intent?.action == Constant.SHOW_TRACKING_FRAGMENT){
            findNavController(R.layout.activity_main).navigate(R.id.action_global_tripFragment)
        }


    }
}