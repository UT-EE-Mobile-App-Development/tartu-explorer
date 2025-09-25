package ee.ut.cs.tartu_explorer.feature.statistics
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
//Statistics Screen
fun StatisticsScreen(onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ){
        // Back Button to "HomeScreen"
        Button(
            onClick = onNavigateBack,
            modifier = Modifier.align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        ) {
            Text("Back")
        }

        //to Delete
        Text("This is statistics Screen",
            textAlign = TextAlign.Start)

        // Bordered box are in the middle
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .border(2.dp, Color.Black)
                .padding(16.dp)

        ){
            // "Statistics" title in the bordered box
            Text(
                text = "Statistics",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.TopCenter)
            )

        }
    }

}