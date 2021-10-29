package com.example.matatumanageruser.data

import com.example.matatumanageruser.utils.OperationStatus
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class KtorRepository  @Inject constructor(
    private val mAuth: FirebaseAuth,
    private  val api: MatManagerApi
): MainRepository{
    override suspend fun login(email: String, password: String): OperationStatus<Driver> {
        TODO("Not yet implemented")
    }

    override suspend fun getBus(plate: String): OperationStatus<Bus> {
        TODO("Not yet implemented")
    }

    override suspend fun getAdmin(adminId: String): OperationStatus<MatAdmin> {
        TODO("Not yet implemented")
    }

    override suspend fun addTrip(trip: Trip): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addStat(statistics: Statistics): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addExpense(expense: Expense): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDriver(driver: Driver): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTrip(trip: Trip): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateStat(statistics: Statistics): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateExpense(expense: Expense): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateBus(bus: Bus): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getTrips(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Trip>> {
        TODO("Not yet implemented")
    }

    override suspend fun getStats(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Statistics>> {
        TODO("Not yet implemented")
    }

    override suspend fun getExpenses(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Expense>> {
        TODO("Not yet implemented")
    }

}