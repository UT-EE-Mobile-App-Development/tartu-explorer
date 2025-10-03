package ee.ut.cs.tartu_explorer.feature.quest

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import ee.ut.cs.tartu_explorer.R
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureDifficulty
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.db.DatabaseProvider
import ee.ut.cs.tartu_explorer.core.data.repository.AdventureRepository
import ee.ut.cs.tartu_explorer.core.ui.theme.components.AnimatedBackground
import ee.ut.cs.tartu_explorer.core.ui.theme.components.CustomBackButton


@Composable
fun QuestScreen(
    onNavigateBack: () -> Unit,
    onNavigateHome: (adventureId: Int) -> Unit
) {
    val db = DatabaseProvider.getDatabase(LocalContext.current)
    val viewModel: QuestViewModel = viewModel(
        factory = QuestViewModelFactory(AdventureRepository(db.adventureDao()))
    )
    val state by viewModel.state.collectAsState()

    //background images
    val backgrounds = listOf(R.drawable.bg1, R.drawable.bg2, R.drawable.bg3)

    AnimatedBackground(backgrounds) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Back button top-left
            CustomBackButton(
                onClick = onNavigateBack,
            )

            // Quest list card centered
            QuestListCard(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.85f)
                    .align(Alignment.Center)
            ) {
                // Scrollable list of quests grouped by difficulty
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    listOf(
                        AdventureDifficulty.VERY_EASY to "Very Easy",
                        AdventureDifficulty.EASY to "Easy",
                        AdventureDifficulty.MEDIUM to "Medium",
                        AdventureDifficulty.HARD to "Hard",
                        AdventureDifficulty.VERY_HARD to "Very Hard"
                    ).forEach { (difficulty, displayName) ->
                        DifficultyRow(
                            onNavigateHome = onNavigateHome,
                            adventures = state.adventures,
                            difficulty = difficulty,
                            displayName = displayName,
                            thumbnailSize = 120.dp
                        )
                    }
                }
            }
        }
    }
}

// Card container for displaying a vertical list of quests or other content.
@Composable
fun QuestListCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(color = Color(0xCCFFFFFF), shape = RoundedCornerShape(16.dp))
            .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}

@Composable
fun DifficultyRow(
    onNavigateHome: (Int) -> Unit,
    adventures: Map<AdventureDifficulty, List<AdventureEntity>>,
    difficulty: AdventureDifficulty,
    displayName: String,
    thumbnailSize: Dp = 100.dp
) {
    val scrollState = rememberScrollState()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(displayName)
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            adventures[difficulty]
                ?.takeIf { it.isNotEmpty() }
                ?.forEach { adventure ->
                    AsyncImage(
                        model = adventure.thumbnailPath,
                        contentDescription = "$displayName levels",
                        modifier = Modifier
                            .size(thumbnailSize)
                            .clickable { onNavigateHome(adventure.id) }
                    )

                } ?: Text("There are no $displayName quests yet.")
        }
    }
}
