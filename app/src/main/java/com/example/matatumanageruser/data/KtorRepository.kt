package com.example.matatumanageruser.data

import android.util.Log
import com.example.matatumanageruser.utils.Constant
import com.example.matatumanageruser.utils.OperationStatus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import javax.inject.Inject

class KtorRepository  @Inject constructor(
    private val mAuth: FirebaseAuth,
    private  val api: MatManagerApi
): MainRepository{
    override suspend fun login(email: String, password: String): OperationStatus<Driver> {
        return try {

            var uId = mAuth.signInWithEmailAndPassword(email, password).await().user!!.uid
            if (!uId.isNullOrEmpty()){
                val response = api.getDrivers(Constant.SINGLE_DRIVER, uId)
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
            val response = api.getBus(Constant.SINGLE_BUS, plate, "a")
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
            val errorBody = response.errorBody()?.charStream()?.readText()?:""
            Log.d("THISSSSSS", errorBody)

            if(response.isSuccessful && result != null && result.toString().contains("true")){
                OperationStatus.Success(result.toString())
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
            val errorBody = response.errorBody()?.charStream()?.readText()?:""
            Log.d("THISSSSSS", errorBody)

            if(response.isSuccessful && result != null && result.toString().contains("true")){
                OperationStatus.Success(result.toString())
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
            if(response.isSuccessful && result != null && result.toString().contains("true")){
                OperationStatus.Success(result.toString())
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun addIssue(issue: Issue): OperationStatus<String> {
        return  try{
            val response = api.createIssue(issue)
            val result = response.body()
            if(response.isSuccessful && result != null && result.has("true")){
                OperationStatus.Success(result.toString())
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
        return  try{
            val response = api.updateTrip(trip)
            val result = response.body()
            if(response.isSuccessful && result != null && result.has("true")){
                OperationStatus.Success(result.toString())
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun updateStat(statistics: Statistics): OperationStatus<String> {
        return  try{
            val response = api.updateStat(statistics)
            val result = response.body()
            if(response.isSuccessful && result != null && result.toString().contains("true")){
                OperationStatus.Success(result.toString())
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }


    override suspend fun updateBus(bus: Bus): OperationStatus<String> {
        return  try{
            val response = api.updateBus(bus)
            val result = response.body()
            if(response.isSuccessful && result != null && result.toString().contains("true")){
                OperationStatus.Success(result.toString())
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun updateIssue(issue: Issue): OperationStatus<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getTrips(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Trip>> {
       return try{
            val response = api.getTrips("", id, startDate, endDate)
            val result = response.body()
            if(response.isSuccessful && !result!!.isEmpty()!!){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

       }catch (e: Exception){
             OperationStatus.Error(e.message ?: "An error occurred")
       }
    }

    override suspend fun getStats(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Statistics>> {
        return  try{
            val response = api.getStats(Constant.DRIVER_STAT, id, "a", "a")
            val result = response.body()
            val errorBody = response.errorBody()?.charStream()?.readText()?:""
            Log.d("THISSSSSS", errorBody)
            if(response.isSuccessful && !result!!.isEmpty()!!){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getExpenses(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Expense>> {
        return  try{
            val response = api.getExpenses(Constant.DRIVER_EXPENSE, id, startDate, endDate)
            val result = response.body()
            if(response.isSuccessful && !result!!.isEmpty()!!){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getIssues(
        type: String,
        id: String,
        startDate: String,
        endDate: String
    ): OperationStatus<List<Issue>> {
        return  try{
            val response = api.getIssues(Constant.DRIVER_ISSUE, id, startDate, endDate)
            val result = response.body()
            if(response.isSuccessful && !result!!.isEmpty()!!){
                OperationStatus.Success(result)
            }else{
                OperationStatus.Error(response.message())
            }

        }catch (e: Exception){
            OperationStatus.Error(e.message ?: "An error occurred")
        }
    }
}