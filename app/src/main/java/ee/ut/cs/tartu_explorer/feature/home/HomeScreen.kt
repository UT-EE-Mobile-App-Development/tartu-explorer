package ee.ut.cs.tartu_explorer.feature.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// Function to add the black outline to white letters
@Composable
fun OutlinedText(
    text: String,
    fontSize: TextUnit = 24.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    textAlign: TextAlign = TextAlign.Start,
    lineHeight: TextUnit = TextUnit.Unspecified
) {
    Box(contentAlignment = Alignment.Center) {
        val offsets = listOf(
            Pair(-1.dp, -1.dp),
            Pair(0.dp, -1.dp),
            Pair(1.dp, -1.dp),
            Pair(-1.dp, 0.dp),
            Pair(1.dp, 0.dp),
            Pair(-1.dp, 1.dp),
            Pair(0.dp, 1.dp),
            Pair(1.dp, 1.dp)
        )

        // Draw black outline layers
        offsets.forEach { (x, y) ->
            Text(
                text = text,
                fontSize = fontSize,
                fontWeight = fontWeight,
                color = Color.Black,
                textAlign = textAlign,
                lineHeight = lineHeight,
                modifier = Modifier.offset(x = x, y = y)
            )
        }

        // Draw white top layer
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = Color.White,
            textAlign = textAlign,
            lineHeight = lineHeight
        )
    }
}



@Composable
fun HomeScreen(
    onNavigateToQuest: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToGame: (adventureId: Int) -> Unit,
    selectedAdventureId: Int? = null,


) {
    // Taking Temp Background images from drawable,
    // Later from used images in game.
    val backgrounds = listOf(
        ee.ut.cs.tartu_explorer.R.drawable.bg1,
        ee.ut.cs.tartu_explorer.R.drawable.bg2,
        ee.ut.cs.tartu_explorer.R.drawable.bg3
    )

    var currentIndex by remember { mutableStateOf(0)}

    // Cycle background every 5 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(60000)
            currentIndex = (currentIndex + 1) % backgrounds.size
        }
    }
        val infiniteTransition = rememberInfiniteTransition(label = "bg_anim")

        val alphaAnim by infiniteTransition.animateFloat(
            initialValue = 0.6f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                tween(60000, easing = LinearEasing),
                RepeatMode.Reverse
            ),
            label = "alpha"
        )

        val offsetAnim by infiniteTransition.animateFloat(
            initialValue = -30f,
            targetValue = 30f,
            animationSpec = infiniteRepeatable(
                tween(12000, easing = LinearEasing),
                RepeatMode.Reverse
            ),
            label = "offset"
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // FULLSCREEN BACKGROUND
            Image(
                painter = painterResource(id = backgrounds[currentIndex]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        // Slight zoom to avoid white edges during movement
                        scaleX = 1.1f
                        scaleY = 1.1f
                    }
                    .offset(y = offsetAnim.dp)  // floating effect
                    .alpha(alphaAnim)           // fading
                    .blur(radius = 7.dp)       // apply blur to background
            )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 200.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Game Tittle in the middle
            OutlinedText(
                text = "TARTU EXPLORER",
                fontSize = 75.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 65.sp
            )

    }
    // FOREGROUND BUTTONS
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 200.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = onNavigateToQuest,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFA500),
                contentColor = Color.Black

            ),
            border = BorderStroke(2.dp, Color.Black)
            ) {
            OutlinedText("QUESTS")
        }
        Button(
            onClick = onNavigateToStatistics,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFA500),
                contentColor = Color.Black

            ),
            border = BorderStroke(2.dp, Color.Black)
        ) {
            OutlinedText("STATISTICS")

        }
        if (selectedAdventureId != null) {
            Text(text = "selected adventure: $selectedAdventureId")
            Button(
                onClick = { onNavigateToGame(selectedAdventureId)},
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFA500),
                    contentColor = Color.Black

                ),
                border = BorderStroke(2.dp, Color.Black)
            ) {
                OutlinedText("PLAY")
            }
        } else {
            Button(enabled = false, onClick = {},
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFA500),
                    contentColor = Color.Black

                ),
                border = BorderStroke(2.dp, Color.Black)
                ) {
                Text("Select an adventure to play")
            }
        }
    }
}