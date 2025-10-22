package ee.ut.cs.tartu_explorer.feature.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ee.ut.cs.tartu_explorer.R
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
                TopAppBar(
                    title = { Text("Statistics") },
                    navigationIcon = { CustomBackButton(onClick = onNavigateBack) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
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
                        val data = s.data
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .fillMaxHeight(0.8f)
                                .align(Alignment.Center)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        color = Color(0xCCFFFFFF),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp))
                                    .padding(16.dp)
                            ) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    item {
                                        Text(
                                            text = "Statistics",
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(bottom = 8.dp),
                                            color = Color.Black
                                        )
                                    }

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
                                            Text(
                                                "No completed quests available.",
                                                color = Color.Black
                                            )
                                        }
                                    } else {
                                        items(data.completedByDifficulty) { entry ->
                                            Row(
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

                                    item { Spacer(Modifier.height(8.dp)) }

                                    item {
                                        Text(
                                            "Required hints (total)",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black
                                        )
                                    }
                                    item {
                                        Text(
                                            "${data.totalHintsUsed}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.Black
                                        )
                                    }

                                    item {
                                        Text(
                                            "Ø Hints per quest (successful)",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black
                                        )
                                    }
                                    item {
                                        Text(
                                            formatDoubleOrDash(data.avgHintsPerQuest),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.Black
                                        )
                                    }

                                    item {
                                        Text(
                                            "Ø Time for an adventure",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black
                                        )
                                    }
                                    item {
                                        Text(
                                            formatDurationOrDash(data.avgAdventureDurationMs),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.Black
                                        )
                                    }

                                    item {
                                        Text(
                                            "Ø Time until first hint",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black
                                        )
                                    }
                                    item {
                                        Text(
                                            formatDurationOrDash(data.avgTimeToFirstHintMs),
                                            style = MaterialTheme.typography.bodyLarge,
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
