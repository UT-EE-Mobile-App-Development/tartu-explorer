package ee.ut.cs.tartu_explorer.feature.game

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import ee.ut.cs.tartu_explorer.core.data.local.db.DatabaseProvider
import ee.ut.cs.tartu_explorer.core.data.repository.AdventureSessionRepository
import ee.ut.cs.tartu_explorer.core.data.repository.GameRepository
import ee.ut.cs.tartu_explorer.core.data.repository.PlayerRepository
import ee.ut.cs.tartu_explorer.core.location.LocationRepository
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun GameScreen(adventureId: Long, onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val db = DatabaseProvider.getDatabase(context)
    val locationRepository = LocationRepository(context)
    val playerRepository = PlayerRepository.from(context)
    val adventureSessionRepository = AdventureSessionRepository(db.adventureSessionDao())

    val viewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(
            adventureId,
            GameRepository(
                db.questDao(),
                db.hintDao(),
                db.hintUsageDao(),
                db.adventureSessionDao(),
                db.questAttemptDao()
            ),
            locationRepository,
            playerRepository,
            adventureSessionRepository
        )
    )
    val state by viewModel.state.collectAsState()
    val locationPermissionToastEvent = viewModel.locationPermissionToastEvent

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProgressBar(
            currentStep = state.currentQuest,
            totalSteps = state.quests.size,
            modifier = Modifier.fillMaxWidth()
        )

        state.hints
            .filterIndexed { index, _ -> index <= state.currentHint }
            .forEach { hint ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(200.dp),

                    ) {
                    if (hint.imageUrl != null) {
                        AsyncImage(
                            model = hint.imageUrl,
                            contentDescription = "Image for Hint ${hint.id}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .weight(1f)
                        )
                    }
                    if (hint.text != null) {
                        Text(text = hint.text)
                    }
                }
            }

        GameControls(
            onNavigateBack,
            onUseHint = { viewModel.requestNextHint() },
            onGuess = { viewModel.guessPosition() },
            modifier = Modifier.fillMaxWidth(),
            hintDisabled = state.currentHint + 1 > state.hints.size - 1,
        )

        if (state.guessState != null) {
            val guess: GuessState = state.guessState!!
            DebugGuessDialog(
                onContinueAnyway = { viewModel.nextQuest() },
                onDismiss = { viewModel.resetDebugGuessDialogue() },
                distance = guess.distanceFromTarget,
                inRange = guess.inRange
            )
        }
        NoPermissionInfo(locationPermissionToastEvent)
    }
}

@Composable
fun ProgressBar(currentStep: Int, totalSteps: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(20.dp)
    ) {
        val progress = currentStep / (totalSteps - 1).toFloat()
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(12.dp)
                .padding(horizontal = 10.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colorStops = arrayOf(
                            progress to Color.Green,
                            progress + 0.01f to Color.White,
                        )
                    )
                )
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            repeat(totalSteps) { index ->
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = if (index < currentStep) Color.Green
                            else if (index == currentStep) Color.Yellow
                            else Color.White,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun GameControls(
    onNavigateBack: () -> Unit,
    onUseHint: () -> Unit,
    onGuess: () -> Unit,
    modifier: Modifier = Modifier,
    hintDisabled: Boolean = false,
) {
    // Back Button to go back to "HomeScreen"
    Column(modifier) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = onNavigateBack,
            ) {
                Text("Back")
            }

            // "Hints" Button
            Button(
                onClick = onUseHint,
                enabled = !hintDisabled,
            ) {
                Text(
                    text = "HINTS"
                )
            }

        }

        // "Guess" Button
        Button(
            onClick = onGuess,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "GUESS"
            )
        }
    }
}

@Composable
fun NoPermissionInfo(locationPermissionToastEvent: SharedFlow<Boolean>) {
    // todo: instead of a toast disable the guess button and show a tooltip
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        locationPermissionToastEvent.collect { it ->
            if (it) {
                val toast =
                    Toast.makeText(context, "permission locations required", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}
