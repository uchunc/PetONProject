package com.woo.peton.core.data.impl

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.woo.peton.domain.repository.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : LocationRepository {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // ViewModel이 바라볼 최신 위치 상태
    private val _latestLocation = MutableStateFlow<Location?>(null)
    override val latestLocation: Flow<Location?> = _latestLocation.asStateFlow()

    override fun updateLocation(location: Location) {
        _latestLocation.value = location
    }

    @SuppressLint("MissingPermission") // 권한은 UI/Service 진입 시 체크
    override fun getLocationUpdates(): Flow<Location> = callbackFlow {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let { trySend(it) }
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 10000L
        ).setMinUpdateIntervalMillis(5000L).build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(location)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}