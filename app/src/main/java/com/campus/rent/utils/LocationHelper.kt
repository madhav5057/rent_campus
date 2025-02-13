package com.campus.rent.utils

import android.content.Context


object LocationHelper {
    fun saveLocation(context: Context, lat: Double, lon: Double) {
        val sharedPreferences = context.getSharedPreferences("UserLocation", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString("latitude", lat.toString())
            .putString("longitude", lon.toString())
            .apply()
    }

    fun getSavedLocation(context: Context): Pair<Double, Double>? {
        val sharedPreferences = context.getSharedPreferences("UserLocation", Context.MODE_PRIVATE)
        val lat = sharedPreferences.getString("latitude", null)
        val lon = sharedPreferences.getString("longitude", null)

        return if (lat != null && lon != null) Pair(lat.toDouble(), lon.toDouble()) else null
    }
}