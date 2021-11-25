package com.example.matatumanageruser.utils

import android.graphics.Color

object Constant {

    val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"

    const val POLYLINE_COLOR = Color.RED
    const val POLYLINE_WIDTH = 8f
    const val MAP_ZOOM = 15f

    const val LOCATION_UPDATE_INTERVAL = 6000L
    const val FASTEST_LOCATION_INTERVAL = 3000L

    val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    val NOTIFICATION_CHANNEL_NAME = "Tracking"
    val NOTIFICATION_ID = 1

    const val SINGLE_DRIVER = "driver"
    const val SINGLE_BUS = "bus"
    const val DRIVER_EXPENSE = "driver_expense"
    const val DRIVER_ISSUE = "driver_issue"
    const val DRIVER_STAT = "driver_stat"
    const val DRIVERS_TRIP = "driver_trip"

    val SHOW_TRACKING_FRAGMENT = "SHOW_TRACKING_FRAGMENT"
    const val REQUEST_CODE_LOCATION_PERMISSION = 0


}