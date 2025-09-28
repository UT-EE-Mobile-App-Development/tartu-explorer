package ee.ut.cs.tartu_explorer.feature.game

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
import ee.ut.cs.tartu_explorer.core.data.repository.GameRepository

@Composable
fun GameScreen(onNavigateBack: () -> Unit) {
    val db = DatabaseProvider.getDatabase(LocalContext.current)
    val viewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(GameRepository(db.questDao(), db.hintDao()))
    )
    val state by viewModel.state.collectAsState()

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
            .filter { it -> it.index <= state.currentHint }
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
                            contentDescription = "Image for Hint ${hint.index}",
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
            onGuess = { viewModel.nextQuest() },
            modifier = Modifier.fillMaxWidth(),
            hintDisabled = state.currentHint >= state.hints.size,
        )
    }
}

@Composable
fun ProgressBar(currentStep: Int, totalSteps: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(20.dp)
    ) {
        val progress = currentStep/(totalSteps-1).toFloat()
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