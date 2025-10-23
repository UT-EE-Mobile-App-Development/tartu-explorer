package ee.ut.cs.tartu_explorer.feature.statistics


import androidx.compose.foundation.background

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ee.ut.cs.tartu_explorer.R
import ee.ut.cs.tartu_explorer.core.ui.theme.Pink40
import ee.ut.cs.tartu_explorer.core.ui.theme.components.AnimatedBackground
import ee.ut.cs.tartu_explorer.core.ui.theme.components.CustomBackButton
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
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Pink40),
                    modifier = Modifier.height(64.dp),
                    windowInsets = WindowInsets(0) //remove extra system padding
                )
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.height(32.dp), // 👈 make it smaller
                    windowInsets = WindowInsets(0) //remove extra system padding
                ) {
                    Text(
                        "Bottom Navigation",
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
                                        color = White,
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
                                                    color = Pink40,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                StatBox(
                                                    title = "Ø Hints per quest",
                                                    value = formatDoubleOrDash(data.avgHintsPerQuest),
                                                    color = Pink40,
                                                    modifier = Modifier.weight(1f)
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
                                                    color = Pink40,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                StatBox(
                                                    title = "Ø Time until first hint",
                                                    value = formatDurationOrDash(data.avgTimeToFirstHintMs),
                                                    color = Pink40,
                                                    modifier = Modifier.weight(1f)
                                                )
                                            }

                                        }
                                    }

                                    item { Spacer(Modifier.height(4.dp)) }
                                    // Completed quests by difficulty
                                    item {
                                        Text(
                                            "Completed quests by difficulty",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black
                                        )
                                    }
                                    if (data.completedByDifficulty.isEmpty()) {
                                        item {
                                            BigStatBox(
                                                title = "No completed quests available.",
                                                value = "NO",
                                                color = Pink40,
                                            )

                                        }
                                    } else {
                                        items(data.completedByDifficulty) { entry ->
                                            Row(
                                                //maybe create a fancier row element eventually
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    entry.difficulty,
                                                    color = Color.Black
                                                )
                                                Text(
                                                    "${entry.count}",
                                                    color = Color.Black
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.3f) // makes each box the correct shape and same size
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
@Composable
fun BigStatBox(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.3f) // makes each box the correct shape and same size
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
