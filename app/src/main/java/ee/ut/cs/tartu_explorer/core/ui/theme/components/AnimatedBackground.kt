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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@Composable
fun AnimatedBackground(
    backgrounds: List<Int>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }

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

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = backgrounds[currentIndex]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = 1.1f
                    scaleY = 1.1f
                }
                .offset(y = offsetAnim.dp)
                .alpha(alphaAnim)
                .blur(7.dp)
        )

        content()
    }
}
