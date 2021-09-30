package com.example.matatumanageruser.data

import com.example.matatumanageradmin.utils.OperationStatus

interface MainRepository {

    suspend fun loginAdmin(email: String, password: String): OperationStatus<MatatuAdmin>
    suspend fun registerAdmin(matatuAdmin: MatatuAdmin, password: String): OperationStatus<String>
    suspend fun registerDriver(driver: Driver, password: String): OperationStatus<String>
    suspend fun addTrip (trip: MatatuTrip): OperationStatus<String>
    suspend fun addMatatu (matatu: Matatu): OperationStatus<String>

}