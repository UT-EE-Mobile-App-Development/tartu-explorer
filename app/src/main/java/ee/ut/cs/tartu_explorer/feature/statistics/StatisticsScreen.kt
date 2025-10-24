package ee.ut.cs.tartu_explorer.feature.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ee.ut.cs.tartu_explorer.R
import ee.ut.cs.tartu_explorer.core.ui.theme.components.AnimatedBackground
import ee.ut.cs.tartu_explorer.core.ui.theme.components.CustomBackButton
import kotlin.collections.listOf
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    vm: StatisticsViewModel = viewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    val backgrounds = listOf(
        R.drawable.bg1,
        R.drawable.bg2
    )

    AnimatedBackground(backgrounds) {
        Scaffold(
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
            containerColor = Color.Transparent
        ) { innerPadding ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (val s = state) {
                    is StatsUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is StatsUiState.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Error loading statistics:\n${s.message}",
                                    textAlign = TextAlign.Center,
                                    color = Color.Black
                                )
                                Spacer(Modifier.height(12.dp))
                                Button(onClick = { vm.refresh() }) {
                                    Text(
                                        "Try again",
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }

                    is StatsUiState.Loaded -> {
                        val data = s.data // hier kommt das StatsData-Objekt vom ViewModel

                        // Card zentriert in der Box (align() ist hier erlaubt, da wir uns im BoxScope befinden)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .fillMaxHeight(1f)
                                .align(Alignment.Center)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        color = Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )

                                    .padding(16.dp)
                            ) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    item { Spacer(Modifier.height(4.dp)) }

                                    //2x2 grid for data
                                    item {
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(12.dp),
                                            modifier = Modifier.padding(0.dp)
                                        ) {
                                            //First row of data
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                StatBox(
                                                    title = "Required hints (total)",
                                                    value = data.totalHintsUsed.toString(),
                                                    color = Color(0xFF4CAF50),
                                                    modifier = Modifier.weight(1f),
                                                    gradientColors = listOf(
                                                        Color(0xFF81C784),
                                                        Color(0xFF4CAF50),
                                                        Color(0xFF2E7D32)
                                                    )
                                                )
                                                StatBox(
                                                    title = "Ø Hints per quest",
                                                    value = formatDoubleOrDash(data.avgHintsPerQuest),
                                                    color = Color(0xFFF44336),
                                                    modifier = Modifier.weight(1f),
                                                    gradientColors = listOf(
                                                        Color(0xFFFF8A80),
                                                        Color(0xFFF44336),
                                                        Color(0xFFC62828)
                                                    )
                                                )
                                            }

                                            //Second row of data
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                StatBox(
                                                    title = "Ø Time for an adventure",
                                                    value = formatDurationOrDash(data.avgAdventureDurationMs),
                                                    color = Color(0xFFFFEB3B),
                                                    modifier = Modifier.weight(1f),
                                                    gradientColors = listOf(
                                                        Color(0xFFFFF59D),
                                                        Color(0xFFFFEB3B),
                                                        Color(0xFFFBC02D)
                                                    )
                                                )
                                                StatBox(
                                                    title = "Ø Time until first hint",
                                                    value = formatDurationOrDash(data.avgTimeToFirstHintMs),
                                                    color = Color(0xFF03A9F4),
                                                    modifier = Modifier.weight(1f),
                                                    gradientColors = listOf(
                                                        Color(0xFF81D4FA),
                                                        Color(0xFF03A9F4),
                                                        Color(0xFF0277BD)
                                                    )
                                                )
                                            }

                                        }
                                    }

                                    item { Spacer(Modifier.height(16.dp)) }


                                    // Completed quests by difficulty

                                    if (data.completedByDifficulty.isEmpty()) {
                                        item {
                                            BigStatBox(
                                                title = "Completed quests by difficulty",
                                                value = "No completed quests available.",
                                                color = Color(0xFF9B687A),
                                                gradientColors = listOf(
                                                    Color(0xFFBC8BA0),
                                                    Color(0xFF9B687A),
                                                    Color(0xFF6D4854)
                                                )
                                            )

                                        }
                                    } else {
                                        // Build a multiline string with all difficulty entries
                                        val difficultySummary = data.completedByDifficulty.joinToString("\n") { entry ->
                                            "${entry.difficulty}: ${entry.count}"
                                        }
                                        item {
                                            BigStatBox(
                                                title = "Completed quests by difficulty",
                                                value = difficultySummary,
                                                color = Color(0xFF9B687A),
                                                gradientColors = listOf(
                                                    Color(0xFFBC8BA0),
                                                    Color(0xFF9B687A),
                                                    Color(0xFF6D4854)
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatDoubleOrDash(value: Double?): String =
    if (value == null) "—" else ((value * 100.0).roundToInt() / 100.0).toString()

private fun formatDurationOrDash(ms: Double?): String {
    if (ms == null) return "—"
    val totalSeconds = (ms / 1000.0).roundToInt()
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return when {
        hours > 0 -> String.format("%dh %02dmin %02ds", hours, minutes, seconds)
        minutes > 0 -> String.format("%dmin %02ds", minutes, seconds)
        else -> String.format("%ds", seconds)
    }
}

//Used to display statistics data
@Composable
fun StatBox(
    title: String,
    value: String,
    color: Color,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.3f) // makes each box the correct shape and same size
            .border(
                width = 4.dp,
                brush = Brush.verticalGradient(gradientColors),
                shape = RoundedCornerShape(24.dp)
            )
            .background(color, RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.Black
        )

        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.Black
        )
    }
}


//used fot the bigger bottom box on the statistics screen
// RIGHT NOW ITS NOT MUCH DIFFERNET FROM THE StatBox BUT
// MAYBE WE NEED TO MAKE LAYOUT CHANGE TO THE BIGGER BOX SO YES
// THIS MAY BE DELETED LATER THEN
@Composable
fun BigStatBox(
    title: String,
    value: String,
    color: Color,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.3f)
            .border(
                width = 5.dp,
                brush = Brush.verticalGradient(gradientColors),
                shape = RoundedCornerShape(24.dp)
            )
            .background(color, RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.Black
        )

        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
            color = Color.Black
        )
    }
}
