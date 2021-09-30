package com.example.matatumanageruser.data



import com.google.firebase.firestore.GeoPoint
import java.util.*


data class  MatatuAdmin(
    var saccoId: String = "",
    var saccoEmail : String = "",
    var compName: String = "",
    var countyName: String = ""
)

data class  Matatu(
    var matId: String = "",
    var matPlate: String = "",
    var matNumber: String = "",
    var brand: String = ""

)

data class Driver(
    var driverId: String = "",
    var driverEmail: String ="",
    var driverName: String = "",
    var drivePhone: Long = 0L
)

data class DayStatistic(
    var dayId: String = "",
    var matId: String = "",
    var day: Date? = null,
    var startTime: String = "",
    var endTime: String = "",
    var location: GeoPoint? = null,
    var maxSpeed: Double = 0.0,
    var distanceCovered: Double = 0.0,
    //var placeCovered: .....
    var amountCollected: Double = 0.0,
    var expenduture: MutableList<MatatuExpenduture> = mutableListOf()
)

data class MatatuExpenduture(
    var expendutureName : String = "",
    var expendutureAmount: Double = 0.0,
    var expendutureDescription: String = ""
)

data class MatatuTrip(
    var tripId: String = "",
    var tripDay: Date? = null,
    var matId: String = "",
    var driverId: String = "",
    var startTime: String = "",
    var endTime: String = "",
    var location: GeoPoint? = null,
    var amountCollected: Double = 0.0
)