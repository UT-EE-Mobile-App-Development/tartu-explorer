package ee.ut.cs.tartu_explorer.feature.statistics

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ee.ut.cs.tartu_explorer.R

@Composable
fun CustomBackButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
            .background(color = Color(0xFFFFA500), shape = RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
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

        offsets.forEach { (x, y) ->
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier
                    .offset(x = x, y = y)
                    .size(24.dp)
            )
        }

        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun StatisticsScreen(onNavigateBack: () -> Unit) {
    // Background images
    val backgrounds = listOf(
        R.drawable.bg1,
        R.drawable.bg2,
        R.drawable.bg3
    )
    var currentIndex by remember { mutableStateOf(0) }

    // Cycle background every 5 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            currentIndex = (currentIndex + 1) % backgrounds.size
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(5000, easing = LinearEasing),
            RepeatMode.Reverse
        )
    )
    val offsetAnim by infiniteTransition.animateFloat(
        initialValue = -30f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            tween(12000, easing = LinearEasing),
            RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Fullscreen moving background
        androidx.compose.foundation.Image(
            painter = painterResource(id = backgrounds[currentIndex]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { scaleX = 1.1f; scaleY = 1.1f }
                .offset(y = offsetAnim.dp)
                .alpha(alphaAnim)
                .blur(radius = 7.dp)
        )

        // Top-left back button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CustomBackButton(
                onClick = onNavigateBack,
            )
        }

        // Centered stats box
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.8f)
                .align(Alignment.Center)
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
}
