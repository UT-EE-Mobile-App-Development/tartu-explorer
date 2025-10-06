package ee.ut.cs.tartu_explorer.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ee.ut.cs.tartu_explorer.core.ui.theme.components.AnimatedBackground
import ee.ut.cs.tartu_explorer.core.ui.theme.components.HomeGameButton
import ee.ut.cs.tartu_explorer.core.ui.theme.components.OutlinedText


@Composable
fun HomeScreen(
    onNavigateToQuest: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToGame: (adventureId: Int) -> Unit,
    selectedAdventureId: Int? = null,
) {
    val backgrounds = listOf(
        ee.ut.cs.tartu_explorer.R.drawable.bg1,
        ee.ut.cs.tartu_explorer.R.drawable.bg2,
        //ee.ut.cs.tartu_explorer.R.drawable.bg3
    )

    AnimatedBackground(backgrounds) {
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

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HomeGameButton("QUESTS", onNavigateToQuest)
                HomeGameButton("STATISTICS", onNavigateToStatistics)

                if (selectedAdventureId != null) {
                    Text("selected adventure: $selectedAdventureId")
                    HomeGameButton("PLAY", { onNavigateToGame(selectedAdventureId) })
                } else {
                    HomeGameButton("Select an adventure to play", {}, enabled = false)
                }
            }
        }
    }
}
