package ee.ut.cs.tartu_explorer.feature.quest

import android.R.attr.scaleX
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import ee.ut.cs.tartu_explorer.core.data.local.db.DatabaseProvider
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureDifficulty
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.repository.AdventureRepository
import ee.ut.cs.tartu_explorer.core.ui.theme.components.CustomBackButton
import kotlinx.coroutines.delay



@Composable
fun QuestScreen(onNavigateBack: () -> Unit, onNavigateHome: (adventureId: Int) -> Unit) {
    val db = DatabaseProvider.getDatabase(LocalContext.current)
    val viewModel: QuestViewModel = viewModel(
        factory = QuestViewModelFactory(AdventureRepository(db.adventureDao()))
    )
    val state by viewModel.state.collectAsState()

    // Background images
    val backgrounds = listOf(
        ee.ut.cs.tartu_explorer.R.drawable.bg1,
        ee.ut.cs.tartu_explorer.R.drawable.bg2,
        ee.ut.cs.tartu_explorer.R.drawable.bg3
    )
    var currentIndex by remember { mutableStateOf(0) }

    // Cycle background every 5 seconds
    // comment: delay should be longer adn the pictures move out of the screen a bit
    // maybe have only 1 picture, bg3 doesn't look the best
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            currentIndex = (currentIndex + 1) % backgrounds.size
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "bg_anim")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(5000, easing = LinearEasing),
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

    Box(modifier = Modifier.fillMaxSize()) {
        // Fullscreen animated background
        Image(
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Top back button
            Row(verticalAlignment = Alignment.CenterVertically) {
                CustomBackButton(onClick = onNavigateBack)
                Spacer(modifier = Modifier.width(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Centered Box containing quest list
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(0.9f)
                    .background(
                        color = Color(0xCCFFFFFF), // semi-transparent white to make quests readable
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp))
                    .padding(25.dp),
                contentAlignment = Alignment.Center
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    listOf(
                        AdventureDifficulty.VERY_EASY to "Very Easy",
                        AdventureDifficulty.EASY to "Easy",
                        AdventureDifficulty.MEDIUM to "Medium",
                        AdventureDifficulty.HARD to "Hard",
                        AdventureDifficulty.VERY_HARD to "Very Hard"
                    ).forEach { (difficulty, displayName) ->
                        DifficultyRow(
                            onNavigateHome = onNavigateHome,
                            adventures = state.adventures,
                            difficulty = difficulty,
                            displayName = displayName,
                            thumbnailSize = 120.dp
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun DifficultyRow(
    onNavigateHome: (Int) -> Unit,
    adventures: Map<AdventureDifficulty, List<AdventureEntity>>,
    difficulty: AdventureDifficulty,
    displayName: String,
    thumbnailSize: Dp = 100.dp
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(displayName)
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            adventures.getValue(difficulty)
                .takeIf { it.isNotEmpty() }
                ?.forEach { adventure ->
                    AsyncImage(
                        model = adventure.thumbnailPath,
                        contentDescription = "$displayName levels",
                        modifier = Modifier
                            .size(thumbnailSize)
                            .clickable { onNavigateHome(adventure.id) }
                    )
                } ?: Text("There are no $displayName quests yet.")
        }
    }
}