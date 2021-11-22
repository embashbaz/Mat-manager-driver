package com.example.matatumanageruser

import android.app.Application
import com.example.matatumanageruser.data.Bus
import com.example.matatumanageruser.data.Driver
import com.example.matatumanageruser.data.Statistics
import com.example.matatumanageruser.data.Trip
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MatManagerUserApp : Application(){

    var driverObject: Driver? = null
    var busObject: Bus? = null
    var statisticsObject: Statistics? = null
    var activeTrip: Trip? = null

}