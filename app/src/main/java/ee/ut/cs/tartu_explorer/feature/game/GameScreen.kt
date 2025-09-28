package ee.ut.cs.tartu_explorer.feature.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import ee.ut.cs.tartu_explorer.R
import ee.ut.cs.tartu_explorer.core.data.local.db.DatabaseProvider
import ee.ut.cs.tartu_explorer.core.data.repository.GameRepository

@Composable
fun GameScreen(onNavigateBack: () -> Unit) {
    val db = DatabaseProvider.getDatabase(LocalContext.current)
    val viewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(GameRepository(db.questDao(), db.hintDao()))
    )
    val state by viewModel.state.collectAsState()

    // State to track current image index
    var currentIndex by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProgressBar(currentIndex)

        state.hints
            .filter { it -> it.index <= state.currentHint }
            .forEach { hint ->
                Column (
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
            onGuess = { },
            modifier = Modifier.fillMaxWidth(),
            hintDisabled = state.currentHint >= state.hints.size,
        )
    }
}

@Composable
fun ProgressBar(progress: Int) {
    // List on Tamp progress bar images
    val images = listOf(
        R.drawable.temp_progress_bar_1,
        R.drawable.temp_progress_bar_2,
        R.drawable.temp_progress_bar_3,
        R.drawable.temp_progress_bar_4,
        R.drawable.temp_progress_bar_5,
        R.drawable.temp_progress_bar_6
    )
    if (progress in 0..5) {
        // Progress bar
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = images[progress]),
                contentDescription = "Progress bar",
            )
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