package ee.ut.cs.tartu_explorer.feature.game

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import ee.ut.cs.tartu_explorer.BuildConfig

/**
 * Dialogue shown after making a guess in a quest,
 * prompting options depending on whether the guess was in range
 *
 * @param onContinueAnyway Callback when choosing to continue anyway
 * @param onDismiss Callback when dismissing the dialogue
 * @param distance Distance of the guess from the target in meters
 * @param inRange Whether the guess was within the acceptable range
 */
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
                    Text("Next Quest")
                }
            },
            title = {
                Text("Quest Completed!")
            },
            text = {
                Text("You've successfully completed this quest. You can now proceed to the next one.")
            },
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                if (BuildConfig.DEV_MODE) {
                    TextButton(
                        onClick = onContinueAnyway
                    ) {
                        Text("Continue Anyway (Debug)")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text("Return")
                }
            },
            title = {
                Text("Not in Range")
            },
            text = {
                Text("Your guess was ${distance.toInt()} meters away. " +
                        if (BuildConfig.DEV_MODE) "The quest will be marked as complete anyway."
                        else "Try again!"
                )
            },
        )
    }
}
