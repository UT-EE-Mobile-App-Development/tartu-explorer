package ee.ut.cs.tartu_explorer.feature.home

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.SessionStatus
import ee.ut.cs.tartu_explorer.core.data.repository.GameRepository
import ee.ut.cs.tartu_explorer.core.data.repository.PlayerRepository
import ee.ut.cs.tartu_explorer.core.ui.theme.MainOrange
import ee.ut.cs.tartu_explorer.core.ui.theme.OrangeGradiantBot
import ee.ut.cs.tartu_explorer.core.ui.theme.OrangeGradiantMid
import ee.ut.cs.tartu_explorer.core.ui.theme.ThemeViewModel
import ee.ut.cs.tartu_explorer.core.ui.theme.components.AnimatedBackground
import ee.ut.cs.tartu_explorer.core.ui.theme.components.HomeGameButton
import ee.ut.cs.tartu_explorer.core.ui.theme.components.OutlinedText
import ee.ut.cs.tartu_explorer.core.util.LevelInfo

@SuppressLint("ContextCastToActivity")
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
    val themeViewModel: ThemeViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    )
    val isDarkMode by themeViewModel.isDarkMode

    LaunchedEffect(selectedAdventureId) {
        if (selectedAdventureId != null) {
            viewModel.prefetchQuestImages(selectedAdventureId, context)
        }
    }


    AnimatedBackground(
        backgrounds = backgrounds,
        isDarkMode = isDarkMode
    ){
        Box(modifier = Modifier.fillMaxSize()) { // Use Box for layering

            // Top Left Profile Switcher
            if (uiState.player != null) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = viewModel::showProfileSwitcher) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Switch Profile",
                            modifier = Modifier.size(48.dp),
                            tint = if (isDarkMode) Color.Black else Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp)) // spacing between icon and switch

                    // Switch below the profile icon
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { themeViewModel.toggleDarkMode() },
                        colors = androidx.compose.material3.SwitchDefaults.colors(
                            checkedTrackColor = MainOrange
                        )
                    )
                }
            }

            // Top Right Level Indicator
            uiState.levelInfo?.let {
                Box(modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
                    CircularLevelIndicator(levelInfo = it)
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

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

            // Show profile switcher dialog if needed
            if (uiState.showProfileSwitcher) {
                ProfileSwitcherDialog(
                    players = uiState.players,
                    activePlayerId = uiState.player?.id,
                    newPlayerName = uiState.newPlayerName,
                    onNewPlayerNameChange = viewModel::onNewPlayerNameChange,
                    onSwitchPlayer = viewModel::switchPlayer,
                    onCreatePlayer = viewModel::createNewPlayer,
                    onDismiss = viewModel::dismissProfileSwitcher,
                    isDarkMode = isDarkMode
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
                    lineHeight = 65.sp,
                    textColor = if (isDarkMode) Color.White else Color.White,
                    outlineColor = if (isDarkMode) Color.Black else Color.Black
                )

                // Welcome message if player exists
                uiState.player?.let {
                    Text("Welcome, ${it.name}!", fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = if (isDarkMode) Color.Black else Color.White)
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HomeGameButton("QUESTS", onNavigateToQuest,isDarkMode = isDarkMode)
                    HomeGameButton("STATISTICS", onNavigateToStatistics, isDarkMode = isDarkMode)

                    if (selectedAdventureId != null && selectedAdventureId > 0) {
                        Text("selected adventure: $selectedAdventureId", color = if (isDarkMode) Color.Black else Color.White)
                        val isCompleted = uiState.adventureStatuses[selectedAdventureId] == SessionStatus.COMPLETED
                        if (isCompleted) {
                            HomeGameButton("Adventure completed", {}, enabled = false)
                        } else {
                            HomeGameButton("PLAY", { onNavigateToGame(selectedAdventureId) }, isDarkMode = isDarkMode, enabled = uiState.readyToPlay)
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
fun ProfileSwitcherDialog(
    players: List<PlayerEntity>,
    activePlayerId: Long?,
    newPlayerName: String,
    onNewPlayerNameChange: (String) -> Unit,
    onSwitchPlayer: (Long) -> Unit,
    onCreatePlayer: () -> Unit,
    onDismiss: () -> Unit,
    isDarkMode: Boolean
) {
    val bgColor = if (isDarkMode) Color(0xFFFFCC80) else Color.DarkGray

    val shape = RoundedCornerShape(18.dp)

    val borderBrush = Brush.verticalGradient(
        if (!isDarkMode)
            listOf(Color.LightGray, Color.Gray, Color(0xFF666666))  // dark gray bottom)
        else
            listOf(OrangeGradiantMid, OrangeGradiantBot)
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Switch or Create Profile",color = if (isDarkMode) Color.Black else Color.White) },
        modifier = Modifier.fillMaxWidth().border(4.dp, borderBrush,shape),
        containerColor = bgColor,
        text = {

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(8.dp).background(bgColor, shape)
            ) {
                // Create new player
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().background(bgColor, shape)
                ) {
                    TextField(
                        value = newPlayerName,
                        onValueChange = onNewPlayerNameChange,
                        placeholder = { Text("New Profile Name", color = Color.Black) },
                        modifier = Modifier.weight(1f).background(bgColor, shape)
                    )
                    Button(
                        onClick = onCreatePlayer,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isDarkMode) Color(0xFF707070) else Color(0xFFFFA64D)
                        ),
                        contentPadding = PaddingValues(0.dp),
                        enabled = newPlayerName.isNotBlank()) {
                        Text("Create", color = if (isDarkMode) Color.Black else Color.White)
                    }
                }
                // Spacer
                Spacer(modifier = Modifier.height(16.dp))
                // List of existing players
                Text("Select a profile:", fontWeight = FontWeight.Bold, color = if (isDarkMode) Color.Black else Color.White)
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.background(bgColor, shape)
                ) {
                    items(players) { player ->
                        Button(
                            onClick = { onSwitchPlayer(player.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!isDarkMode) Color(0xFF707070) else Color(0xFFFFA64D)
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            enabled = player.id != activePlayerId
                        ) {
                            Text(player.name + if (player.id == activePlayerId) " (Active)" else "", color = if (isDarkMode) Color.Black else Color.White)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = if (isDarkMode) Color.Black else Color.White)
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
