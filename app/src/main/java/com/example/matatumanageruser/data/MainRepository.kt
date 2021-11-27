package com.example.matatumanageruser.data

import com.example.matatumanageruser.utils.OperationStatus


interface MainRepository {

    suspend fun login(email: String, password: String): OperationStatus<Driver>
    suspend fun resetPassword(email: String): OperationStatus<String>
    suspend fun logOut(): OperationStatus<String>
    suspend fun getBus(plate: String): OperationStatus<Bus>
    suspend fun getAdmin(adminId: String): OperationStatus<MatAdmin>

    suspend fun addTrip (trip: Trip): OperationStatus<String>
    suspend fun addStat (statistics: Statistics): OperationStatus<String>
    suspend fun addExpense(expense: Expense): OperationStatus<String>
    suspend fun addIssue(issue: Issue): OperationStatus<String>

    suspend fun updateDriver(driver: Driver): OperationStatus<String>//ToDo: implement this
    suspend fun updateTrip(trip: Trip): OperationStatus<String>
    suspend fun updateStat(statistics: Statistics): OperationStatus<String>
    suspend fun updateBus(bus: Bus): OperationStatus<String>
    suspend fun updateIssue(issue: Issue): OperationStatus<String>

    suspend fun getTrips(type: String, id: String, startDate: String, endDate: String): OperationStatus<List<Trip>>
    suspend fun getStats(type: String, id: String, startDate: String, endDate: String): OperationStatus<List<Statistics>>
    suspend fun getExpenses(type: String, id: String, startDate: String, endDate: String): OperationStatus<List<Expense>>

    suspend fun getIssues(type: String, id: String, startDate: String, endDate: String): OperationStatus<List<Issue>>


}