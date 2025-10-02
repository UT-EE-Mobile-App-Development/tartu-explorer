package ee.ut.cs.tartu_explorer.core.location

import android.content.Context
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

class LocationRepository(context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @Suppress("MissingPermission")
    suspend fun getLastLocation() = try {
        fusedLocationClient.lastLocation.await()
    } catch (e: Exception) {
        Log.e("LocationRepository", "couldn't get location ${e.message}")
        null
    }
}