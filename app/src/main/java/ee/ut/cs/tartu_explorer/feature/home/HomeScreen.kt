package ee.ut.cs.tartu_explorer.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onNavigateToQuest: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToGame: (adventureId: Int) -> Unit,
    selectedAdventureId: Int? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Game Tittle in the middle
        Text(
            text = "TARTU EXPLORER",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 40.sp
        )

    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(onClick = onNavigateToQuest) {
            Text("Quest")
        }
        Button(onClick = onNavigateToStatistics) {
            Text("Statistics")
        }
        if (selectedAdventureId != null) {
            Text(text = "selected adventure: $selectedAdventureId")
            Button(onClick = { onNavigateToGame(selectedAdventureId) }) {
                Text("Play")
            }
        } else {
            Button(enabled = false, onClick = {}) {
                Text("Select an adventure to play")
            }
        }
    }
}