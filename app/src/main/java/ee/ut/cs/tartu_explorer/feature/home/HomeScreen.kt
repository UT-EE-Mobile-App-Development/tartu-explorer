package ee.ut.cs.tartu_explorer.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ee.ut.cs.tartu_explorer.R
import ee.ut.cs.tartu_explorer.core.data.local.db.DatabaseProvider
import ee.ut.cs.tartu_explorer.core.data.local.entities.SessionStatus
import ee.ut.cs.tartu_explorer.core.data.repository.GameRepository
import ee.ut.cs.tartu_explorer.core.data.repository.PlayerRepository
import ee.ut.cs.tartu_explorer.core.ui.theme.components.AnimatedBackground
import ee.ut.cs.tartu_explorer.core.ui.theme.components.HomeGameButton
import ee.ut.cs.tartu_explorer.core.ui.theme.components.OutlinedText
import ee.ut.cs.tartu_explorer.core.util.LevelInfo

@Composable
fun HomeScreen(
    onNavigateToQuest: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToGame: (adventureId: Long) -> Unit,
    onNavigateToDev: () -> Unit,
    selectedAdventureId: Long? = null,
) {
    val context = LocalContext.current
    val db = DatabaseProvider.getDatabase(context)
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(
            PlayerRepository.from(context),
            GameRepository(db.questDao(), db.hintDao(), db.hintUsageDao(), db.adventureSessionDao(), db.questAttemptDao())
        )
    )
    val uiState by viewModel.uiState.collectAsState()

    val backgrounds = listOf(
        R.drawable.bg1,
        R.drawable.bg2
    )

    AnimatedBackground(backgrounds) {
        Box(modifier = Modifier.fillMaxSize()) { // Use Box for layering

            // Top Right Level Indicator
            uiState.levelInfo?.let {
                Box(modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
                    CircularLevelIndicator(levelInfo = it)
                }
            }

            // Show name prompt dialog if needed
            if (uiState.showNamePrompt) {
                PlayerNamePromptDialog(
                    playerName = uiState.playerNameInput,
                    onPlayerNameChange = viewModel::onPlayerNameChange,
                    onSave = viewModel::savePlayer
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 200.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedText(
                    text = "TARTU EXPLORER",
                    fontSize = 75.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 65.sp
                )

                // Welcome message if player exists
                uiState.player?.let {
                    Text("Welcome, ${it.name}!", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HomeGameButton("QUESTS", onNavigateToQuest)
                    HomeGameButton("STATISTICS", onNavigateToStatistics)

                    if (selectedAdventureId != null && selectedAdventureId > 0) {
                        Text("selected adventure: $selectedAdventureId")
                        val isCompleted = uiState.adventureStatuses[selectedAdventureId] == SessionStatus.COMPLETED
                        if (isCompleted) {
                            HomeGameButton("Adventure completed", {}, enabled = false)
                        } else {
                            HomeGameButton("PLAY", { onNavigateToGame(selectedAdventureId) })
                        }
                    } else {
                        HomeGameButton("Select an adventure to play", {}, enabled = false)
                    }
                }
            }

            // Dev Panel Button at the bottom right
            TextButton(
                onClick = onNavigateToDev,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Text("Dev-Panel")
            }
        }
    }
}

@Composable
fun PlayerNamePromptDialog(
    playerName: String,
    onPlayerNameChange: (String) -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Dialog cannot be dismissed without input */ },
        title = { Text("Welcome to Tartu Explorer!") },
        text = {
            Column {
                Text("Please enter your name to begin.")
                TextField(
                    value = playerName,
                    onValueChange = onPlayerNameChange,
                    singleLine = true,
                    placeholder = { Text("Your Name") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onSave,
                enabled = playerName.isNotBlank() // Button only enabled when text is entered
            ) {
                Text("Save")
            }
        }
    )
}

@Composable
fun CircularLevelIndicator(levelInfo: LevelInfo, size: Dp = 80.dp) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
        CircularProgressIndicator(
            progress = 1f, // Full circle background
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            strokeWidth = 6.dp,
            trackColor = Color.Transparent
        )
        CircularProgressIndicator(
            progress = levelInfo.progressPercentage,
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 6.dp,
            strokeCap = StrokeCap.Round
        )
        Text(
            text = "${levelInfo.level}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
