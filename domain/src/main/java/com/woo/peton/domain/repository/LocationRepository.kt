package com.woo.peton.domain.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    val latestLocation: Flow<Location?>

    fun updateLocation(location: Location)

    fun getLocationUpdates(): Flow<Location>
}