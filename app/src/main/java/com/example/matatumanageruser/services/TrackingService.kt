package com.example.matatumanageruser.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.matatumanageruser.MainActivity
import com.example.matatumanageruser.MatManagerUserApp
import com.example.matatumanageruser.R
import com.example.matatumanageruser.data.Bus
import com.example.matatumanageruser.data.MainRepository
import com.example.matatumanageruser.data.Statistics
import com.example.matatumanageruser.utils.Constant
import com.example.matatumanageruser.utils.Constant.FASTEST_LOCATION_INTERVAL
import com.example.matatumanageruser.utils.Constant.LOCATION_UPDATE_INTERVAL
import com.example.matatumanageruser.utils.DispatcherProvider
import com.example.matatumanageruser.utils.convertToJsonArray
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import javax.inject.Inject


//Most of this code were gotten form this file
// https://github.com/philipplackner/RunningAppYT/blob/GettingLocationUpdates/app/src/main/java/com/androiddevs/runningappyt/services/TrackingService.kt


@AndroidEntryPoint
class TrackingService :  LifecycleService(){
    var serviceIsStarted = true


    @set:Inject
    lateinit var repository: MainRepository

    @set:Inject
    lateinit var dispatcherProvider: DispatcherProvider


    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {

        val currentLocation = MutableLiveData<Location>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<MutableList<MutableList<LatLng>>>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                Constant.ACTION_START_OR_RESUME_SERVICE ->{
                    if (serviceIsStarted){
                        startForegroundService()
                        serviceIsStarted = false
                    }else{

                    }
                }

                Constant.ACTION_PAUSE_SERVICE -> {
                    pauseService()
                }

                Constant.ACTION_STOP_SERVICE -> {
                    stopService(intent)
                }
                else ->  {
                    Log.d("Service Action" , "I don't know")
                }

            }

        }


        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if(isTracking) {
            if(TrackingUtils.hasLocationPermissions(this)) {
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun pauseService() {
        isTracking.postValue(false)

    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if(isTracking.value!!) {
                result?.locations?.let { locations ->
                    for(location in locations) {
                        addPathPoint(location)
                        currentLocation.postValue(location)

                    }
                }
            }
        }
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
                updateStat()
            }
        }
    }

    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))


    private fun startForegroundService(){

        addEmptyPolyline()
        isTracking.postValue(true)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, Constant.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_baseline_directions_bus_24)
            .setContentTitle("Mat Manager")
            .setContentText("Bus in service")
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

    fun updateStat(){
        var todayStat = ( application as MatManagerUserApp).statisticsObject
        if (todayStat != null && currentLocation.value != null){
            todayStat.locationLat = currentLocation.value!!.latitude
            todayStat.locationLng = currentLocation.value!!.longitude
            todayStat.comment = "active"
            if (pathPoints.value!!.isNotEmpty()) {
                todayStat.pathPoints = convertToJsonArray(pathPoints.value!!)
                todayStat.distance = getDistance()
                updateStatInDb(todayStat)
                (application as MatManagerUserApp).statisticsObject = todayStat
                val bus = ( application as MatManagerUserApp).busObject
                updateBusInDb(bus!!)

            }

        }

        if (todayStat == null){
            //stopService(intent)
        }

    }

    fun updateStatInDb(stat: Statistics){
        lifecycleScope.launch(dispatcherProvider.io){
            lifecycleScope.launch(dispatcherProvider.io){
               repository.updateStat(stat)
            }
        }

    }

    fun updateBusInDb(bus: Bus){
        lifecycleScope.launch(dispatcherProvider.io){
            bus.locationLat = currentLocation.value!!.latitude
            bus.locationLng = currentLocation.value!!.longitude
            repository.updateBus(bus)
        }
    }

    fun getDistance(): Double{

        var distanceInMeters = 0.0
        for(polyline in pathPoints.value!!) {
            distanceInMeters += TrackingUtils.calculatePolylineLength(polyline).toDouble()
        }

        return distanceInMeters/1000

    }


}