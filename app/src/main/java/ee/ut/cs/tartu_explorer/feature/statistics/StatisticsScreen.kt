package ee.ut.cs.tartu_explorer.feature.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ee.ut.cs.tartu_explorer.R
import ee.ut.cs.tartu_explorer.core.ui.theme.components.AnimatedBackground
import ee.ut.cs.tartu_explorer.core.ui.theme.components.CustomBackButton


@Composable
fun StatisticsScreen(onNavigateBack: () -> Unit) {
    val backgrounds = listOf(
        R.drawable.bg1,
        R.drawable.bg2,
        //R.drawable.bg3
    )

    AnimatedBackground(backgrounds) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Top row with back button + spacing
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.TopStart)   // Place at the top start of the Box
                    .padding(16.dp)         // Add padding from edges
            ) {
                CustomBackButton(onClick = onNavigateBack)
                Spacer(modifier = Modifier.width(8.dp))
            }

            // Centered stats card
            StatsCard(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxHeight(0.8f)
                    .align(Alignment.Center)
            )
        }
    }
}


// Statistics information
@Composable
private fun StatsCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color = Color(0xCCFFFFFF), shape = RoundedCornerShape(16.dp))
            .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Statistics",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Your stats will appear here.",
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
