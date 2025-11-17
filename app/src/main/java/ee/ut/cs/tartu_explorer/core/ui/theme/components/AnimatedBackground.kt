package ee.ut.cs.tartu_explorer.core.ui.theme.components
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix


// A full-screen animated background that cycles through a list of drawable resources.
@Composable
fun AnimatedBackground(
    backgrounds: List<Int>,
    modifier: Modifier = Modifier,
    grayscaleLevel: Float = 0f, // 0 = full color, 1 = fully grayscale
    isDarkMode: Boolean = false, // true if dark mode
    content: @Composable () -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }

    // Automatically switch the background every 60 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(60000)
            currentIndex = (currentIndex + 1) % backgrounds.size
        }
    }
    // Create an infinite animation transition
    val infiniteTransition = rememberInfiniteTransition(label = "bg_anim")

    // Animate image alpha to subtly pulse between 0.6 and 1.0
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(10000, easing = LinearEasing), //10 sec to change transparency
            RepeatMode.Reverse
        ),
        label = "alpha"
    )

    // Animate vertical offset between -20.dp and +20.dp for slow drift effect
    val offsetAnim by infiniteTransition.animateFloat(
        initialValue = -30f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            tween(10000, easing = LinearEasing), //10 sec to move up and down
            RepeatMode.Reverse
        ),
        label = "offset"
    )

    Box(modifier = modifier.fillMaxSize()) {

        // Background image with zoom, blur, alpha, and motion
        Image(
            painter = painterResource(id = backgrounds[currentIndex]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                // Slightly zooms the image in to prevent edges from showing when offset
                .graphicsLayer {
                    scaleX = 1.25f
                    scaleY = 1.25f
                }
                .offset(y = offsetAnim.dp)
                // Apply alpha only if NOT dark mode
                .then(if (isDarkMode) Modifier.alpha(alphaAnim) else Modifier)
                .blur(7.dp)
                .drawWithContent {
                    drawContent() // draw the original image
                    if (!isDarkMode) {
                        drawRect(
                            color = Color.Black.copy(alpha = 0.8f) // adjust alpha for darkness
                        )
                    }
                }

        )


        content()
    }
}
