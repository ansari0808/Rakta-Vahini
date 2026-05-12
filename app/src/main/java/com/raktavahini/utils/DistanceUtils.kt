package com.raktavahini.utils

import android.location.Location
import kotlin.math.*

object DistanceUtils {

    /**
     * Haversine formula — returns distance in km between two lat/lng points.
     */
    fun distanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    fun isWithinRadius(
        userLat: Double, userLon: Double,
        donorLat: Double, donorLon: Double,
        radiusKm: Double
    ): Boolean = distanceKm(userLat, userLon, donorLat, donorLon) <= radiusKm
}
