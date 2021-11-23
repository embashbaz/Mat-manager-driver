package com.example.matatumanageruser.utils

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.common.io.CharStreams
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken


fun convertToJsonArray(pathPoint: MutableList<MutableList<LatLng>>): String {
    Log.d("Locationnnnn", Gson().toJson(pathPoint))
  return Gson().toJson(pathPoint)
}

fun fromJsonToPolylines(loc: String): MutableList<MutableList<LatLng>>{
    val gson = Gson()
    val itemType = object : TypeToken<MutableList<MutableList<LatLng>>>() {}.type
    return gson.fromJson(loc, itemType)
}




