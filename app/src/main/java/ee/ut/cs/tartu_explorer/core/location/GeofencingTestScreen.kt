package ee.ut.cs.tartu_explorer.core.location

import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ee.ut.cs.tartu_explorer.core.PermissionBox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@SuppressLint("InlinedApi")
@Composable
fun GeofencingScreen() {
    val permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    // Requires at least coarse permission
    PermissionBox(
        permissions = permissions
    ) {
        PermissionBox(
            permissions = listOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
        ) {
            GeofencingControls()
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
private fun GeofencingControls() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val geofenceManager = remember { GeofenceManager(context) }
    var geofenceTransitionEventInfo by remember {
        mutableStateOf("")
    }

    DisposableEffect(androidx.lifecycle.compose.LocalLifecycleOwner.current) {
        onDispose {
            scope.launch(Dispatchers.IO) {
                geofenceManager.deregisterGeofence()
            }
        }
    }

    // Register a local broadcast to receive activity transition updates
    GeofenceBroadcastReceiver(systemAction = CUSTOM_INTENT_GEOFENCE) { event ->
        geofenceTransitionEventInfo = event
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        GeofenceList(geofenceManager)
        Button(
            onClick = {
                if (geofenceManager.geofenceList.isNotEmpty()) {
                    geofenceManager.registerGeofence()
                } else {
                    Toast.makeText(
                        context,
                        "Please add at least one geofence to monitor",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            },
        ) {
            Text(text = "Register Geofences")
        }

        Button(
            onClick = {
                scope.launch(Dispatchers.IO) {
                    geofenceManager.deregisterGeofence()
                }
            },
        ) {
            Text(text = "Deregister Geofences")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = geofenceTransitionEventInfo)
    }
}

@Composable
fun GeofenceList(geofenceManager: GeofenceManager) {
    // for geofences
    val checkedGeoFence1 = remember { mutableStateOf(false) }
    val checkedGeoFence2 = remember { mutableStateOf(false) }
    val checkedGeoFence3 = remember { mutableStateOf(false) }
    val checkedGeoFence4 = remember { mutableStateOf(false) }

    Text(text = "Available Geofence")
    Row(
        Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checkedGeoFence1.value,
            onCheckedChange = { checked ->
                if (checked) {
                    geofenceManager.addGeofence(Quest("statue_of_libery", 40.689403968838015, -74.04453795094359))
                } else {
                    geofenceManager.removeGeofence("statue_of_libery")
                }
                checkedGeoFence1.value = checked
            },
        )
        Text(text = "Statue of Liberty")
    }
    Row(
        Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checkedGeoFence2.value,
            onCheckedChange = { checked ->
                if (checked) {
                    geofenceManager.addGeofence(Quest("eiffel_tower", 48.85850, 2.29455))
                } else {
                    geofenceManager.removeGeofence("eiffel_tower")
                }
                checkedGeoFence2.value = checked
            },
        )
        Text(text = "Eiffel Tower")
    }
    Row(
        Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checkedGeoFence3.value,
            onCheckedChange = { checked ->
                if (checked) {
                    geofenceManager.addGeofence(Quest("vatican_city", 41.90238, 12.45398))
                } else {
                    geofenceManager.removeGeofence("vatican_city")
                }
                checkedGeoFence3.value = checked
            },
        )
        Text(text = "Vatican City")
    }
    Row(
        Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checkedGeoFence4.value,
            onCheckedChange = { checked ->
                if (checked) {
                    geofenceManager.addGeofence(Quest("raatuse", 58.382615, 26.732212))
                } else {
                    geofenceManager.removeGeofence("raatuse")
                }
                checkedGeoFence4.value = checked
            },
        )
        Text(text = "Raatuse")
    }
}