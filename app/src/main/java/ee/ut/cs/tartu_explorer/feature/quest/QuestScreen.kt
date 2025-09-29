package ee.ut.cs.tartu_explorer.feature.quest

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import ee.ut.cs.tartu_explorer.core.data.local.db.DatabaseProvider
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureDifficulty
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.repository.AdventureRepository

@Composable
fun QuestScreen(onNavigateBack: () -> Unit) {
    val db = DatabaseProvider.getDatabase(LocalContext.current)
    val viewModel: QuestViewModel = viewModel(
        factory = QuestViewModelFactory(AdventureRepository(db.adventureDao()))
    )
    val state by viewModel.state.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        DifficultyRow(
            adventures = state.adventures,
            difficulty = AdventureDifficulty.VERY_EASY,
            displayName = "Very Easy"
        )

        DifficultyRow(
            adventures = state.adventures,
            difficulty = AdventureDifficulty.EASY,
            displayName = "Easy"
        )

        DifficultyRow(
            adventures = state.adventures,
            difficulty = AdventureDifficulty.MEDIUM,
            displayName = "Medium"
        )

        DifficultyRow(
            adventures = state.adventures,
            difficulty = AdventureDifficulty.HARD,
            displayName = "Hard"
        )

        DifficultyRow(
            adventures = state.adventures,
            difficulty = AdventureDifficulty.VERY_HARD,
            displayName = "Very Hard"
        )

    Button(
        onClick = onNavigateBack,
        modifier = Modifier
    ) {
        Text("Back")
    }
}

}

@Composable
fun DifficultyRow(
    adventures: Map<AdventureDifficulty, List<AdventureEntity>>,
    difficulty: AdventureDifficulty,
    displayName: String
) {
    val scrollState = rememberScrollState()
    Row {
        Column {
            Text(displayName)
            Row(
                modifier = Modifier.horizontalScroll(scrollState) // enables scrolling
            ) {
                adventures.getValue(difficulty)
                    .takeIf { it.isNotEmpty() }
                    ?.forEach { adventure ->
                        AsyncImage(
                            model = adventure.thumbnailPath,
                            contentDescription = "$displayName levels",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(4.dp)
                        )
                    } ?: Text("There are no $displayName quests yet.")
            }
        }
    }
}