package com.example.matatumanageruser.data

import kotlinx.serialization.Serializable

@Serializable
data class MatAdmin(
    var matAdminId: String ="",
    var name: String = "",
    var email: String = "",
    var phoneNumber: Long = 0L,
    var address: String = "",
    var licenseType: String = "",
    var licenseNumber: String = "",
    var licenseLink: String = "",
    var status: String = "",
    var comment: String = "",
    var dateCreated: String = ""

    )

@Serializable
data class Driver(
    var driverId: String = "",
    var managerId: String = "",
    var cardId : String = "",
    var permitNumber: String = "",
    var pictureLink : String = "",
    var permitLink: String = "",
    var name: String = "",
    var email: String = "",
    var phoneNumber: Long = 0L,
    var address: String = "",
    var status: String = "",
    var comment: String = "",
    val dateCreated: String = ""

)

@Serializable
data class Bus(
    var plate: String = "",
    var managerId: String = "",
    var identifier: String = "",
    var carModel : String = "",
    var pathPoints: String = "",
    var locationLat: Double = 0.0,
    var locationLng: Double = 0.0,
    var status: String = "",
    var comment: String = "",
    var dateCreated: String = ""

)

@Serializable
data class Trip(
    var tripId: String = "",
    var date: String = "",
    var busPlate: String = "",
    var driverId: String = "",
    var pathPoints: String = "",
    var moneyCollected: Double = 0.0,
    var timeStarted: String = "",
    var timeEnded: String = "",
    var tripStatus: String = "",
    var comment: String = ""
)

@Serializable
data class Statistics(
    var dayId: String = "",
    var busPlate: String = "",
    var driverId: String = "",
    var pathPoints: String = "",
    var locationLat: Double = 0.0,
    var locationLng: Double = 0.0,
    var timeStarted: String = "",
    var distance: Double = 0.0,
    var amount: Double = 0.0,
    var expense: Double = 0.0,
    var timeEnded: String = "",
    var maxSpeed: Double = 0.0,
    var numberTrip : Int = 0,
    var comment: String = ""
)

@Serializable
data class Expense(
    var expenseId: String = "",
    var date: String = "",
    var busPlate: String = "",
    var driverId: String = "",
    var amount: Double = 0.0,
    var reason: String = "",
    var comment: String = ""
)

@Serializable
data class Issue(
    var issueId: String = "",
    var date: String = "",
    var status: String = "",
    var busPlate: String = "",
    var driverId: String = "",
    var reason: String = "",
    var comment: String = ""

)