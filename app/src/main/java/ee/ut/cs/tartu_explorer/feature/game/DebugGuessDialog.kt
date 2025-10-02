package ee.ut.cs.tartu_explorer.feature.game

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugGuessDialog(
    onContinueAnyway: () -> Unit,
    onDismiss: () -> Unit,
    distance: Float,
    inRange: Boolean
) {
    if (inRange) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = onContinueAnyway
                ) {
                    Text("next quest")
                }
            },
            title = {
                Text("Guess Confirmation")
            },
            text = {
                Text("You completed this quest and can continue to the next quest")
            },
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = onContinueAnyway
                ) {
                    Text("continue anyway")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text("return")
                }
            },
            title = {
                Text("Guess Confirmation")
            },
            text = {
                Text("Not in range. Distance to target $distance")
            },

            )
    }
}