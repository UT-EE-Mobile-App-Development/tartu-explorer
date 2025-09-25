package ee.ut.cs.tartu_explorer.core.location

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import ee.ut.cs.tartu_explorer.core.ui.theme.TartuExplorerTheme

@OptIn(ExperimentalPermissionsApi::class)
private sealed interface Permission {
    data class PermissionSingle(val inner: PermissionState) : Permission
    data class PermissionMultiple(val inner: MultiplePermissionsState) : Permission
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionsActivity(modifier: Modifier = Modifier) {
    val approximateLocationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )
    val fineLocationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
        ),
    )
    val backgroundLocationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
    )

    if (!approximateLocationPermissionState.status.isGranted) {
        // We request the fine location because the approximate location is not enough.
        // The differentiation is to show the user a more fitting reason.
        LocationPermissionDialog(
            permission = Permission.PermissionMultiple(fineLocationPermissionState),
            text = "Tartu Explorer needs access to your location to properly function",
            modifier
        )
    } else if (!fineLocationPermissionState.allPermissionsGranted) {
        LocationPermissionDialog(
            permission = Permission.PermissionMultiple(fineLocationPermissionState),
            text = "Tartu Explorer needs access to your precise location to properly function. You currently only allow access to your approximate location",
            modifier
        )
    } else if (!backgroundLocationPermissionState.status.isGranted) {
        LocationPermissionDialog(
            permission = Permission.PermissionSingle(backgroundLocationPermissionState),
            text = "To automatically validate your Quests. Tartu Explorer needs access to your location when running in the background",
            modifier
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LocationPermissionDialog(permission: Permission, text: String, modifier: Modifier= Modifier) {
    Column (
        modifier = modifier
            .background(Color.Yellow, shape = RoundedCornerShape(16.dp))
            .clickable(onClick = {
                when (permission) {
                    is Permission.PermissionSingle -> permission.inner.launchPermissionRequest()
                    is Permission.PermissionMultiple -> permission.inner.launchMultiplePermissionRequest()
                }
            })
            .padding(20.dp)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.titleMedium
        )
        // the app may only ask for the user's location twice
        // see https://developer.android.com/training/permissions/requesting#workflow_for_requesting_permissions
        val deniedTwice = when (permission) {
            is Permission.PermissionSingle -> !permission.inner.status.isGranted && !permission.inner.status.shouldShowRationale
            is Permission.PermissionMultiple -> !permission.inner.allPermissionsGranted && !permission.inner.shouldShowRationale
        }
        if (deniedTwice) {
            Text(text="You can grant the permissions in your System Settings", style = MaterialTheme.typography.labelMedium)
        } else {
            Text(text="Tap to grant permissions", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationPermissionActivityPreview() {
    TartuExplorerTheme {
        LocationPermissionsActivity()
    }
}