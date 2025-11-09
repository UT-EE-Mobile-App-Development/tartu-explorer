package ee.ut.cs.tartu_explorer.feature.quest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import ee.ut.cs.tartu_explorer.R
import ee.ut.cs.tartu_explorer.core.data.local.db.DatabaseProvider
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureDifficulty
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.SessionStatus
import ee.ut.cs.tartu_explorer.core.data.repository.AdventureRepository
import ee.ut.cs.tartu_explorer.core.data.repository.AdventureStatusDetails
import ee.ut.cs.tartu_explorer.core.data.repository.GameRepository
import ee.ut.cs.tartu_explorer.core.ui.theme.Pink40

import ee.ut.cs.tartu_explorer.core.ui.theme.components.AnimatedBackground
import ee.ut.cs.tartu_explorer.core.ui.theme.components.CustomBackButton
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestScreen(
    onNavigateBack: () -> Unit,
    onNavigateHome: (adventureId: Long) -> Unit
) {
    val context = LocalContext.current
    val db = DatabaseProvider.getDatabase(context)
    val viewModel: QuestViewModel = viewModel(
        factory = QuestViewModelFactory(
            AdventureRepository.from(context),
            GameRepository(db.questDao(), db.hintDao(), db.hintUsageDao(), db.adventureSessionDao(), db.questAttemptDao())
        )
    )
    val state by viewModel.state.collectAsState()

    val backgrounds = listOf(R.drawable.bg1, R.drawable.bg2)

    AnimatedBackground(backgrounds) {
        Scaffold(
            containerColor = Color.Transparent, // this removes the white background
            // Top bar
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Statistics",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    },
                    navigationIcon = {
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomBackButton(onClick = onNavigateBack)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Transparent),
                    modifier = Modifier.height(96.dp),
                    windowInsets = WindowInsets(0) //remove extra system padding
                )
            },
            // So i can make it small, that the body has more room
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.height(32.dp),
                    windowInsets = WindowInsets(0), //remove extra system padding
                    containerColor = Transparent
                ) {
                    Text(
                        "",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            },
            )
        { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(Transparent)
                    .padding(innerPadding),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    AdventureDifficulty.VERY_EASY to "Very Easy",
                    AdventureDifficulty.EASY to "Easy",
                    AdventureDifficulty.MEDIUM to "Medium",
                    AdventureDifficulty.HARD to "Hard",
                    AdventureDifficulty.VERY_HARD to "Very Hard"
                ).forEach { (difficulty, displayName) ->
                    QuestCardWithDifficulty(
                        questName = displayName,
                        onNavigateHome = onNavigateHome,
                        adventures = state.adventures,
                        adventureStatuses = state.adventureStatuses,
                        adventureStatusDetails = state.adventureStatusDetails,
                        difficulty = difficulty,
                        displayName = displayName,
                        thumbnailSize = 120.dp
                    )
                }
            }
        }
    }
}





@Composable
fun DifficultyRow(
    onNavigateHome: (Long) -> Unit,
    adventures: Map<AdventureDifficulty, List<AdventureEntity>>,
    adventureStatuses: Map<Long, SessionStatus>,
    adventureStatusDetails: Map<Long, AdventureStatusDetails>,
    difficulty: AdventureDifficulty,
    displayName: String,
    thumbnailSize: Dp = 100.dp
) {
    val scrollState = rememberScrollState()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            adventures[difficulty]
                ?.takeIf { it.isNotEmpty() }
                ?.forEach { adventure ->
                    val status = adventureStatuses[adventure.id]
                    val details = adventureStatusDetails[adventure.id]
                    val borderColor = when (status) {
                        SessionStatus.IN_PROGRESS -> Color.Yellow
                        SessionStatus.COMPLETED -> Color.Green
                        else -> Color.Transparent
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = adventure.thumbnailPath,
                            contentDescription = "$displayName levels",
                            modifier = Modifier
                                .size(thumbnailSize)
                                .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                                .clickable { onNavigateHome(adventure.id) }
                        )
                        if (details != null) {
                            Text("Hints: ${details.hintsUsed}", color = Color.Black)
                            Text("Quests: ${details.completedQuests}/${details.totalQuests}", color = Color.Black)
                            Text("Time: ${formatDuration(details.durationMs)}", color = Color.Black)
                        }
                    }
                } ?: Text(
                "There are no $displayName quests yet.",
                color = Color.Black
            )
        }
    }
}

fun formatDuration(millis: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(millis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    return when {
        hours > 0 -> String.format("%dh %02dmin %02ds", hours, minutes, seconds)
        minutes > 0 -> String.format("%dmin %02ds", minutes, seconds)
        else -> String.format("%ds", seconds)
    }
}

@Composable
fun QuestCardWithDifficulty(
    questName: String,
    onNavigateHome: (Long) -> Unit,
    adventures: Map<AdventureDifficulty, List<AdventureEntity>>,
    adventureStatuses: Map<Long, SessionStatus>,
    adventureStatusDetails: Map<Long, AdventureStatusDetails>,
    difficulty: AdventureDifficulty,
    displayName: String,
    thumbnailSize: Dp = 100.dp
) {
    var expanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), //padding between difficulties
        colors = CardDefaults.elevatedCardColors(
            containerColor = Pink40
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(20.dp) // makes the initial(unopened boxes bigger)
        ) {
            Text(
                text = questName,
                style = MaterialTheme.typography.titleMedium
            )
        }

            // Show DifficultyRow when expanded
            AnimatedVisibility(visible = expanded) {
                DifficultyRow(
                    onNavigateHome = onNavigateHome,
                    adventures = adventures,
                    adventureStatuses = adventureStatuses,
                    adventureStatusDetails = adventureStatusDetails,
                    difficulty = difficulty,
                    displayName = displayName,
                    thumbnailSize = thumbnailSize
                )
            }
        }
    }



