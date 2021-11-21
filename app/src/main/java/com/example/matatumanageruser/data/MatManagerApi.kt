package com.example.matatumanageruser.data

import com.example.util.*
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MatManagerApi {

    @POST(CREATE_TRIPS)
    suspend fun createTrip(@Body trip: Trip): Response<JsonObject>

    @POST(CREATE_EXPENSES)
    suspend fun createExpense(@Body expense: Expense): Response<JsonObject>

    @POST(CREATE_STATS)
    suspend fun createStat(@Body statistics: Statistics): Response<JsonObject>

    @POST(CREATE_ISSUES)
    suspend fun createIssue(@Body issue: Issue): Response<JsonObject>



    @POST(UPDATE_BUSES)
    suspend fun updateBus(@Body bus: Bus): Response<JsonObject>

    @POST(UPDATE_TRIPS)
    suspend fun updateTrip(@Body trip: Trip): Response<JsonObject>

    @POST(UPDATE_STATS)
    suspend fun updateStat(@Body statistics: Statistics): Response<JsonObject>


    @GET(MAT_ADMIN)
    suspend fun getAdmin(
        @Query("adminId") adminId: String

    ): Response<MatAdmin>

    @GET(DRIVERS)
    suspend fun getDrivers(
        @Query("type") type: String,
        @Query("id") adminId: String

    ): Response<Driver>

    @GET(BUSES)
    suspend fun getBus(
        @Query("type") type: String,
        @Query("id") adminId: String

    ): Response<Bus>

    @GET(TRIPS)
    suspend fun getTrips(
        @Query("type") type: String,
        @Query("id") id: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String

    ): Response<List<Trip>>

    @GET(STATS)
    suspend fun getStats(
        @Query("type") type: String,
        @Query("id") id: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<List<Statistics>>

    @GET(EXPENSES)
    suspend fun getExpenses(
        @Query("type") type: String,
        @Query("id") id: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<List<Expense>>

    @GET(ISSUES)
    suspend fun getIssues(
        @Query("type") type: String,
        @Query("id") id: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<List<Issue>>

}