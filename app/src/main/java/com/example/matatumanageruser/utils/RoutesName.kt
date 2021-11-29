package com.example.util

import com.example.matatumanageruser.utils.web

const val BASE_URL = web

const val API_VERSION = "/v1"
const val MAT_ADMIN = "$API_VERSION/mat_admin"
const val DRIVERS = "$API_VERSION/driver"
const val BUSES = "$API_VERSION/bus"
const val TRIPS = "$API_VERSION/trip"
const val STATS = "$API_VERSION/stat"
const val EXPENSES = "$API_VERSION/expense"
const val CREATE_TRIPS = "$TRIPS/create"
const val CREATE_STATS = "$STATS/create"
const val CREATE_EXPENSES = "$EXPENSES/create"
const val UPDATE_BUSES = "$BUSES/update"

const val UPDATE_DRIVERS = "$DRIVERS/update"

const val UPDATE_TRIPS = "$TRIPS/update"
const val UPDATE_STATS = "$STATS/update"

const val ISSUES = "$API_VERSION/issue"
const val CREATE_ISSUES = "$ISSUES/create"
const val UPDATE_ISSUES = "$ISSUES/update"


