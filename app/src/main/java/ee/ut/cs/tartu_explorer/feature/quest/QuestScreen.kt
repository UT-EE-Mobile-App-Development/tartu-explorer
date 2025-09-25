package ee.ut.cs.tartu_explorer.feature.quest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
//Quests Screen
fun QuestScreen(onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Button(
            onClick = onNavigateBack,
            modifier = Modifier.align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
            ) {
            Text("Back")
        }
        //to Delete
        Text("This is Quests Screen")
    }
}