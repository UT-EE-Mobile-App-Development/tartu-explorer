package ee.ut.cs.tartu_explorer.feature.home
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    onNavigateToQuest: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToGame: () -> Unit
) {
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
        Button(onClick = onNavigateToGame) {
            Text("Play")
        }
    }
}