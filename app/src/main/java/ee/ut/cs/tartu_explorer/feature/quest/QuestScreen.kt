package ee.ut.cs.tartu_explorer.feature.quest
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.SubcomposeAsyncImage
import ee.ut.cs.tartu_explorer.R
import ee.ut.cs.tartu_explorer.core.data.local.db.DatabaseProvider
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureDifficulty
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.SessionStatus
import ee.ut.cs.tartu_explorer.core.data.repository.AdventureRepository
import ee.ut.cs.tartu_explorer.core.data.repository.AdventureStatusDetails
import ee.ut.cs.tartu_explorer.core.data.repository.GameRepository
import ee.ut.cs.tartu_explorer.core.data.repository.PlayerRepository
import ee.ut.cs.tartu_explorer.core.ui.theme.ThemeViewModel
import ee.ut.cs.tartu_explorer.core.ui.theme.components.AnimatedBackground
import ee.ut.cs.tartu_explorer.core.ui.theme.components.CustomBackButton
import ee.ut.cs.tartu_explorer.core.ui.theme.components.OutlinedText
import java.util.concurrent.TimeUnit
import kotlin.Boolean


/**
 * Screen that displays quests categorized by difficulty levels.
 *
 * @param onNavigateBack Callback to navigate back.
 * @param onNavigateHome Callback to navigate to the home screen with a specific adventure ID.
 */
@SuppressLint("ContextCastToActivity")
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
            GameRepository(db.questDao(), db.hintDao(), db.hintUsageDao(), db.adventureSessionDao(), db.questAttemptDao()),
            PlayerRepository.from(context)
        )
    )
    val state by viewModel.state.collectAsState()
    var expandedDifficulty by remember { mutableStateOf<AdventureDifficulty?>(null) }
    //for the logic that 1 can only be expanded at a time
    val backgrounds = listOf(R.drawable.bg1, R.drawable.bg2)
    val themeViewModel: ThemeViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    )
    val isDarkMode by themeViewModel.isDarkMode

    AnimatedBackground(
        backgrounds = backgrounds,
        isDarkMode = isDarkMode
    ) {
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
                            OutlinedText(
                                text = "Quests",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                textColor = Color.White,
                                outlineColor = Color.Black
                            )
                        }
                    },
                    navigationIcon = {
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomBackButton(onClick = onNavigateBack, isDarkMode = isDarkMode)
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
                        thumbnailSize = 130.dp,
                        expanded = (expandedDifficulty == difficulty),
                        onExpandToggle = {
                            expandedDifficulty =
                                if (expandedDifficulty == difficulty) null // collapse if clicked when already opened
                                else difficulty // otherwise expand
                        }
                    )
                }
            }
        }
    }
}

/**
 * Composable that displays a row of adventures for a specific difficulty level.
 *
 * @param onNavigateHome Callback to navigate to the home screen with a specific adventure ID.
 * @param adventures Map of adventures categorized by difficulty.
 * @param adventureStatuses Map of adventure IDs to their session statuses.
 * @param adventureStatusDetails Map of adventure IDs to their status details.
 * @param difficulty The difficulty level to display.
 * @param displayName The display name for the difficulty level.
 * @param thumbnailSize The size of the adventure thumbnails.
 */
@Composable
fun DifficultyRow(
    onNavigateHome: (Long) -> Unit,
    adventures: Map<AdventureDifficulty, List<AdventureEntity>>,
    adventureStatuses: Map<Long, SessionStatus>,
    adventureStatusDetails: Map<Long, AdventureStatusDetails>,
    difficulty: AdventureDifficulty,
    displayName: String,
    thumbnailSize: Dp
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.padding(bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(start = 8.dp),
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
                        SubcomposeAsyncImage(
                            model = adventure.thumbnailPath,
                            contentDescription = adventure.title,
                            modifier = Modifier
                                .width(thumbnailSize+20.dp).height(thumbnailSize-30.dp)
                                .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp))//makes the pictures corners rounded as well
                                .clickable { onNavigateHome(adventure.id) },
                            contentScale = ContentScale.Crop, //so the picture fits in the box
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(40.dp),
                                        strokeWidth = 4.dp
                                    )
                                }
                            }
                        )
                        if (details != null) {
                            Text(
                                "Quests: ${details.completedQuests}/${details.totalQuests}" +
                                        "  Hints: ${details.hintsUsed} ", color = Color.Black
                            )
                            //Text("Time: ${formatDuration(details.durationMs)}", color = Color.Black)
                        }
                    }
                } ?: Text(
                "There are no $displayName quests yet.",
                color = Color.Black
            )
        }
    }
}

/**
 * Formats a duration in milliseconds to a human-readable string.
 *
 * @param millis Duration in milliseconds.
 * @return Formatted duration string.
 */
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

/**
 * Composable that displays a card for a specific difficulty level, which can be expanded to show
 * adventures of that difficulty.
 *
 * @param questName The name of the quest/difficulty level.
 * @param onNavigateHome Callback to navigate to the home screen with a specific adventure ID.
 * @param adventures Map of adventures categorized by difficulty.
 * @param adventureStatuses Map of adventure IDs to their session statuses.
 * @param adventureStatusDetails Map of adventure IDs to their status details.
 * @param difficulty The difficulty level to display.
 * @param displayName The display name for the difficulty level.
 * @param thumbnailSize The size of the adventure thumbnails.
 * @param expanded Whether the card is expanded to show adventures.
 * @param onExpandToggle Callback to toggle the expanded state.
 * @param isDarkMode Whether dark mode is enabled.
 */
@SuppressLint("ContextCastToActivity")
@Composable
fun QuestCardWithDifficulty(
    questName: String,
    onNavigateHome: (Long) -> Unit,
    adventures: Map<AdventureDifficulty, List<AdventureEntity>>,
    adventureStatuses: Map<Long, SessionStatus>,
    adventureStatusDetails: Map<Long, AdventureStatusDetails>,
    difficulty: AdventureDifficulty,
    displayName: String,
    thumbnailSize: Dp,
    expanded: Boolean,
    onExpandToggle: () -> Unit,
    isDarkMode: Boolean = false
) {
    val themeViewModel: ThemeViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    )
    val isDarkMode by themeViewModel.isDarkMode

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 3.dp,
                brush = when (difficulty) {
                    //custom gradient for each color
                    AdventureDifficulty.VERY_EASY -> Brush.verticalGradient(
                        colors = if (isDarkMode) {
                            listOf(Color(0xFFFFB833), Color(0xFFF7A71A), Color(0xFFE09A00))
                        } else {
                            listOf(Color(0xFFFFAD4D),  Color(0xFFD27A1F), Color(0xFFB46617))
                        }
                    )
                    AdventureDifficulty.EASY -> Brush.verticalGradient(
                        colors = if (isDarkMode) {
                            listOf(Color(0xFFF7B21A), Color(0xFFF1A11A), Color(0xFFDB8F00))
                        } else {
                            listOf(Color(0xFFD9984A), Color(0xFFB97318), Color(0xFFA36000))
                        }
                    )
                    AdventureDifficulty.MEDIUM -> Brush.verticalGradient(
                        colors = if (isDarkMode) {
                            listOf(Color(0xFFF0A200), Color(0xFFE09200), Color(0xFFD08000))
                        } else {
                            listOf(Color(0xFFCC8F40), Color(0xFFAD6B1B), Color(0xFF99570F))
                        }
                    )
                    AdventureDifficulty.HARD -> Brush.verticalGradient(
                        colors = if (isDarkMode) {
                            listOf(Color(0xFFE59A00), Color(0xFFD08200), Color(0xFFC07000))
                        } else {
                            listOf(Color(0xFFB26A00), Color(0xFF995500), Color(0xFF7F4300))
                        }
                    )
                    AdventureDifficulty.VERY_HARD -> Brush.verticalGradient(
                        colors = if (isDarkMode) {
                            listOf(Color(0xFFD58500), Color(0xFFBF7400), Color(0xFFAB6200))
                        } else {
                            listOf(Color(0xFF9C5C00), Color(0xFF7F4700), Color(0xFF663600))
                        }
                    )
                },
                shape = RoundedCornerShape(12.dp) // ensures corners match card
            )
            .clip(RoundedCornerShape(12.dp)), // keeps content inside rounded border
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isDarkMode) {
                when (difficulty) {
                    AdventureDifficulty.VERY_EASY -> Color(0xFFF7A71A)
                    AdventureDifficulty.EASY -> Color(0xFFF1A11A)
                    AdventureDifficulty.MEDIUM -> Color(0xFFE09200)
                    AdventureDifficulty.HARD -> Color(0xFFD08200)
                    AdventureDifficulty.VERY_HARD -> Color(0xFFBF7400)
                }
            } else {
                when (difficulty) {
                    AdventureDifficulty.VERY_EASY -> Color(0xFFD27A1F)//not happy with this
                    AdventureDifficulty.EASY -> Color(0xFFBF741F)
                    AdventureDifficulty.MEDIUM -> Color(0xFFB76F1A)
                    AdventureDifficulty.HARD -> Color(0xFF9C5F00)
                    AdventureDifficulty.VERY_HARD -> Color(0xFF8A4F00)
                }
            }
        )
    ) {



        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandToggle() }
                .padding(if (expanded) 18.dp else 18.dp)
        // makes the initial(unopened boxes)bigger, (can make the title smaller after expanding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            )
            {
                OutlinedText(
                    text = questName,
                    fontSize = 16.sp,
                    textColor = if (isDarkMode) Color.White else Color.Black,
                    outlineColor = if (isDarkMode) Color.Black else Color.White
                )
                // Rotating arrow
                OutlinedText(
                    text = if (expanded) "▲" else "▼",
                    fontSize = 24.sp,
                    textColor = if (isDarkMode) Color.White else Color.Black,
                    outlineColor = if (isDarkMode) Color.Black else Color.White
                )
            }

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
