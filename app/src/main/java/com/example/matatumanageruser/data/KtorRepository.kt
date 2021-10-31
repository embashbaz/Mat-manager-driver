package com.example.matatumanageruser.data

import com.example.matatumanageruser.utils.OperationStatus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class KtorRepository  @Inject constructor(
    private val mAuth: FirebaseAuth,
    private  val api: MatManagerApi
): MainRepository{
    override suspend fun login(email: String, password: String): OperationStatus<Driver> {
        return try {

            var uId = mAuth.signInWithEmailAndPassword(email, password).await().user!!.uid
            if (!!uId.isNullOrEmpty()){
                val response = api.getDrivers("", uId)
                val result = response.body()
                if(response.isSuccessful && result != null){
                    OperationStatus.Success(result)
                }else{
                    OperationStatus.Error(response.message())
                }
            }else{
                OperationStatus.Error("Pleas make sure the account exit")
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getBus(plate: String): OperationStatus<Bus> {
        return  try{
            val response = api.getBus("", plate)
            val result = response.body()
            if(response.isSuccessful && result != null){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getAdmin(adminId: String): OperationStatus<MatAdmin> {
        return  try{
            val response = api.getAdmin(adminId)
            val result = response.body()
            if(response.isSuccessful && result != null){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun addTrip(trip: Trip): OperationStatus<String> {
        return  try{
            val response = api.createTrip(trip)
            val result = response.body()
            if(response.isSuccessful && result != null && !result.isNullOrEmpty()){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun addStat(statistics: Statistics): OperationStatus<String> {
        return  try{
            val response = api.createStat(statistics)
            val result = response.body()
            if(response.isSuccessful && result != null && !result.isNullOrEmpty()){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun addExpense(expense: Expense): OperationStatus<String> {
        return  try{
            val response = api.createExpense(expense)
            val result = response.body()
            if(response.isSuccessful && result != null && !result.isNullOrEmpty()){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
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