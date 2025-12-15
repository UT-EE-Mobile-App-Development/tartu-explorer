package ee.ut.cs.tartu_explorer.feature.game

import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import ee.ut.cs.tartu_explorer.R
import ee.ut.cs.tartu_explorer.core.data.local.db.DatabaseProvider
import ee.ut.cs.tartu_explorer.core.data.repository.AdventureSessionRepository
import ee.ut.cs.tartu_explorer.core.data.repository.GameRepository
import ee.ut.cs.tartu_explorer.core.data.repository.PlayerRepository
import ee.ut.cs.tartu_explorer.core.location.LocationRepository
import ee.ut.cs.tartu_explorer.core.ui.theme.MainOrange
import ee.ut.cs.tartu_explorer.core.ui.theme.OrangeGradiantBot
import ee.ut.cs.tartu_explorer.core.ui.theme.OrangeGradiantMid
import ee.ut.cs.tartu_explorer.core.ui.theme.OrangeGradiantTop
import ee.ut.cs.tartu_explorer.core.ui.theme.ThemeViewModel
import ee.ut.cs.tartu_explorer.core.ui.theme.components.AnimatedBackground
import ee.ut.cs.tartu_explorer.core.ui.theme.components.CustomBackButton
import ee.ut.cs.tartu_explorer.feature.weather.WeatherState
import ee.ut.cs.tartu_explorer.feature.weather.WeatherViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random


/**
 * Main game screen composable.
 *
 * @param adventureId ID of the adventure that is being played
 * @param onNavigateBack Callback to navigate back to the previous screen
 */
@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(adventureId: Long, onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val db = DatabaseProvider.getDatabase(context)
    val locationRepository = LocationRepository(context)
    val playerRepository = PlayerRepository.from(context)
    val adventureSessionRepository = AdventureSessionRepository(db.adventureSessionDao())

    val viewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(
            adventureId,
            GameRepository(
                db.questDao(),
                db.hintDao(),
                db.hintUsageDao(),
                db.adventureSessionDao(),
                db.questAttemptDao()
            ),
            locationRepository,
            playerRepository,
            adventureSessionRepository
        )
    )

    val state by viewModel.state.collectAsState()
    val locationPermissionToastEvent = viewModel.locationPermissionToastEvent

    val backgrounds = listOf(R.drawable.bg1, R.drawable.bg2)

    var showHintPopup by remember { mutableStateOf(false) }
    var showWeatherPopup by remember { mutableStateOf(false) }
    var currentHintText by remember { mutableStateOf("") }

    var showBlueCircleOnMap by remember { mutableStateOf(false) }
    var showCompletionPopup by remember { mutableStateOf(false) }

    val targetLatLng = state.quests.getOrNull(state.currentQuest)?.let { quest ->
        LatLng(quest.latitude, quest.longitude)}

    val themeViewModel: ThemeViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    )
    val isDarkMode by themeViewModel.isDarkMode

    AnimatedBackground(
        backgrounds = backgrounds,
        isDarkMode = isDarkMode) {
        Box(modifier = Modifier.fillMaxSize()) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .zIndex(1f)
            ) {
                CustomBackButton(onClick = onNavigateBack, isDarkMode = isDarkMode)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.85f)
                    .align(Alignment.Center)
                    .background(Color(0xFFFFFAEE), RoundedCornerShape(16.dp))
                    .border(
                        BorderStroke(
                            3.dp,
                            Brush.verticalGradient(
                                colors = when {
                                    !isDarkMode -> listOf(
                                        Color(0xFFFFA533),
                                        Color(0xFFCC7A00),
                                        Color(0xFFB36700)
                                    )

                                    else -> listOf(Color(0xFFF7B21A), Color(0xFFF1A11A), Color(0xFFDB8F00)
                                    )
                                }
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

            ) {
                Image(
                    painter = painterResource(id = R.drawable.splash_bg),
                    contentDescription = null,
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(16.dp)), // clip to rounded corners
                    contentScale = ContentScale.Crop
                )
                // Overlay for dark mode
                if (!isDarkMode) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color(0x80000000)) // semi-transparent black (50%)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                        //.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(                   // OUTER BOX
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(
                                    3.dp,
                                    Brush.verticalGradient(
                                        colors = when {
                                            !isDarkMode -> listOf(
                                                Color(0xFFFFA533),
                                                Color(0xFFCC7A00),
                                                Color(0xFFB36700)
                                            )
                                            else -> listOf(Color(0xFFF7B21A), Color(0xFFF1A11A), Color(0xFFDB8F00)
                                            )
                                        }
                                    )
                                ),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .background(MainOrange, RoundedCornerShape(20.dp))
                            .padding(3.dp)      // padding inside outer box
                    ) {

                        Box(               // INNER BOX
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFFAEE), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {

                            ProgressBar( 
                                currentStep = state.currentQuest,
                                totalSteps = state.quests.size,
                                isDarkMode = isDarkMode,
                                showCompletionPopup = showCompletionPopup,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // Hint Image Viewer
                    var currentHintIndex by remember { mutableStateOf(0) }
                    val unlockedHints = state.hints.filterIndexed { index, _ -> index <= state.currentHint }

                    LaunchedEffect(state.currentHint) {
                        currentHintIndex = state.currentHint.coerceAtLeast(0)
                        currentHintText = unlockedHints.getOrNull(currentHintIndex)?.text ?: ""
                    }

                    if (unlockedHints.isNotEmpty()) {
                        val safeIndex = currentHintIndex.coerceIn(0, unlockedHints.lastIndex)
                        val currentHint = unlockedHints[safeIndex]

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    BorderStroke(
                                        3.dp,
                                        Brush.verticalGradient(
                                            colors = when {
                                                !isDarkMode -> listOf(
                                                    Color(0xFFFFA533),
                                                    Color(0xFFCC7A00),
                                                    Color(0xFFB36700)
                                                )

                                                else -> listOf(Color(0xFFF7B21A), Color(0xFFF1A11A), Color(0xFFDB8F00)
                                                )
                                            }
                                        )
                                    ),shape = RoundedCornerShape(16.dp))
                                .height(300.dp)
                                .clip(RoundedCornerShape(16.dp))
                        ) {
                            if (currentHint.imageUrl != null) {
                                AsyncImage(
                                    model = currentHint.imageUrl,
                                    contentDescription = "Hint Image ${currentHint.id}",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.matchParentSize()
                                )
                            } else {
                                Box(
                                    modifier = Modifier.matchParentSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No image available")
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .fillMaxHeight()
                                    .width(72.dp)
                                    .clickable(enabled = safeIndex > 0) { currentHintIndex = safeIndex - 1 },
                                contentAlignment = Alignment.CenterStart
                            ) { if (safeIndex > 0) Text("◀", fontSize = 30.sp, color = Color.White.copy(alpha = 0.9f)) }

                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .fillMaxHeight()
                                    .width(72.dp)
                                    .clickable(enabled = safeIndex < unlockedHints.lastIndex) { currentHintIndex = safeIndex + 1 },
                                contentAlignment = Alignment.CenterEnd
                            ) { if (safeIndex < unlockedHints.lastIndex) Text("▶", fontSize = 30.sp, color = Color.White.copy(alpha = 0.9f)) }

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .background(Color.Black.copy(alpha = 0.35f))
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                val totalHints = unlockedHints.size
                                val hintsUsed = currentHintIndex+1

                                Text("Hint $hintsUsed/$totalHints", color = Color.White)
                            }
                        }

                        // Update current hint text for popup
                        currentHintText = currentHint.text ?: ""
                    }

                    // Game Controls
                    GameControls(
                        onUseHint = {
                            when (state.currentHint) {
                                0 -> viewModel.requestNextHint()
                                1 -> {
                                    viewModel.requestNextHint()
                                    showBlueCircleOnMap = true
                                }
                            }
                        },
                        onGuess = {
                            viewModel.guessPosition()
                            if (state.guessState?.inRange == true) {
                                showBlueCircleOnMap = false}
                                  },
                        currentHintText = currentHintText,
                        modifier = Modifier.fillMaxWidth(),
                        hintDisabled = state.currentHint > state.hints.size-2,
                        showHintPopup = { showHintPopup = it },
                        showWeatherPopup = { showWeatherPopup  = it },
                        showBlueCircleOnMap = showBlueCircleOnMap,
                        targetLatLng = targetLatLng,
                        isDarkMode = isDarkMode
                    )


                    if (state.guessState != null) {
                        val guess = state.guessState!!
                        DebugGuessDialog(
                            onContinueAnyway = {
                                val wasLastQuest = state.currentQuest == state.quests.size - 1

                                // Track quest attempt, award EP, and move to next quest
                                if (guess.inRange) {
                                    // Normal completion: in radius
                                    viewModel.completeQuestNormally(andMoveToNext = true)
                                } else {
                                    // Debug: Force quest completion even if not in range
                                    viewModel.forceQuestCompletion(andMoveToNext = true)
                                }

                                showBlueCircleOnMap = false

                                if (wasLastQuest) {
                                    showCompletionPopup = true
                                }
                                               },
                            onDismiss = { 
                                // Track quest attempt with actual result (usually false), but don't move to next quest
                                viewModel.completeQuestNormally(andMoveToNext = false)
                                viewModel.resetDebugGuessDialogue() 
                            },
                            distance = guess.distanceFromTarget,
                            inRange = guess.inRange
                        )
                    }

                    NoPermissionInfo(locationPermissionToastEvent)
                }
            }

            // Centered Hint Popup
            if (showHintPopup && currentHintText.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable {showHintPopup = false},
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(3.dp, MainOrange),
                        modifier = Modifier.padding(32.dp)
                            .clickable {showHintPopup = false}
                    ) {
                        Text(
                            text = currentHintText,
                            color = Color.White,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }

            }
            if (showCompletionPopup) {

                val shape = RoundedCornerShape(16.dp)

                // Same colors as your ProfileSwitcher
                val bgColor = if (isDarkMode) Color(0xFFFFCC80) else Color.DarkGray

                val borderBrush = Brush.verticalGradient(
                    if (!isDarkMode)
                        listOf(Color.LightGray, Color.Gray, Color(0xFF666666))
                    else
                        listOf( OrangeGradiantMid, OrangeGradiantBot)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .padding(32.dp)
                            .border(4.dp, borderBrush, shape)
                            .background(bgColor, shape)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "You Completed The Adventure!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) Color.Black else Color.White
                        )

                        Text(
                            text = "Great job!\nYou solved all quests!",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = if (isDarkMode) Color.Black else Color.White
                        )

                        // Styled button, same as your other dialogs
                        Button(
                            onClick = {
                                showCompletionPopup = false
                                onNavigateBack()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!isDarkMode) Color(0xFF707070) else Color(0xFFFFA64D)
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                "Finish",
                                color = if (isDarkMode) Color.Black else Color.White
                            )
                        }
                    }
                }
            }
            // Centered Hint Popup
            if (showWeatherPopup && targetLatLng != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable {showWeatherPopup = false},
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(3.dp, MainOrange),
                        modifier = Modifier.padding(32.dp)
                            .clickable {showWeatherPopup = false}
                    ) {
                        WeatherCard(lat = targetLatLng.latitude, lon = targetLatLng.longitude)

                    }
                }
            }

        }

    }

}


/**
 * Horizontal progress bar with map marker icons and between segments,
 * showing the current progress in the adventure.
 *
 * @param currentStep The current step (quest) index
 * @param totalSteps The total number of steps (quests)
 * @param modifier Modifier for styling
 * @param isDarkMode Whether dark mode is enabled
 * @param showCompletionPopup Whether to show the final marker as completed
 */
@Composable
fun ProgressBar(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false,
    showCompletionPopup: Boolean = false
) {
    val listState = rememberLazyListState()

    // Scroll automatically after the 5th marker
    LaunchedEffect(currentStep) {
        if (currentStep >= 5) {
            listState.animateScrollToItem(currentStep)
        }
    }

    LazyRow(
        state = listState,
        modifier = modifier.padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(totalSteps) { index ->
            val isCompleted = index < currentStep || (showCompletionPopup && index == totalSteps - 1)
            val isCurrent = !showCompletionPopup && index == currentStep

            val color = when {
                isCurrent -> Color(0xFFB71C1C)     // red active marker
                isCompleted -> Color(0xFF4CAF50)   // green completed marker
                else -> if (isDarkMode) Color.LightGray else Color.DarkGray
            }

            val scale by animateFloatAsState(
                targetValue = if (isCurrent) 1.25f else 1f,
                label = ""
            )

            MapMarkerIconTriangle(color = color, scale = scale)

            // Road segment except after last marker
            if (index < totalSteps - 1) {
                RoadSegment(
                    color = if (index < currentStep) Color(0xFF4CAF50) else if (isDarkMode) Color.LightGray else Color.DarkGray
                )
            }
        }
    }
}

/**
 * Map marker icon composed of a circle head and a triangle tip.
 *
 * @param color Color of the marker
 * @param scale Scale factor for the size of the marker
 */
@Composable
fun MapMarkerIconTriangle(color: Color, scale: Float) {
    Column(
        modifier = Modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Circle head
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(color)
        )

        // Triangle tip
        Canvas(
            modifier = Modifier
                .size(14.dp)
                .graphicsLayer(rotationZ = 180f) // point downward
        ) {
            val path = Path().apply {
                moveTo(size.width / 2f, 0f)
                lineTo(0f, size.height)
                lineTo(size.width, size.height)
                close()
            }
            drawPath(path, color = color)
        }
    }
}

/**
 * A single segment between map markers.
 *
 * @param color Color of the segment
 */
@Composable
fun RoadSegment(color: Color) {
    Box(
        modifier = Modifier
            .width(30.dp)
            .height(4.dp)
            .background(color, RoundedCornerShape(2.dp))
    )
}

/**
 * Game control buttons for using hints and making guesses.
 *
 * @param onUseHint Callback when the "HINTS" button is clicked
 * @param onGuess Callback when the "GUESS" button is clicked
 * @param currentHintText The text of the current hint to show in the popup
 * @param modifier Modifier for styling
 * @param hintDisabled Whether the hint button should be disabled
 * @param showHintPopup Callback to show/hide the hint popup
 * @param showWeatherPopup Callback to show/hide the weather popup
 * @param showBlueCircleOnMap Whether to show the blue circle on the map
 * @param targetLatLng The target location's latitude and longitude
 * @param isDarkMode Whether dark mode is enabled
 */
@SuppressLint("MissingPermission")
@Composable
fun GameControls(
    onUseHint: () -> Unit,
    onGuess: () -> Unit,
    currentHintText: String,
    modifier: Modifier = Modifier,
    hintDisabled: Boolean = false,
    showHintPopup: (Boolean) -> Unit,
    showWeatherPopup: (Boolean) -> Unit,
    showBlueCircleOnMap: Boolean = false,
    targetLatLng: LatLng? = null,
    isDarkMode: Boolean
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var hasPermission by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasPermission = granted
            if (granted) {
                // Move camera to current location once
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                update = com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(latLng, 16f),
                                durationMs = 1000
                            )
                        }
                    }
                }
            }
        }
    )
    LaunchedEffect(Unit) { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onGuess, colors = ButtonDefaults.buttonColors(
                containerColor = if (isDarkMode) MainOrange else Color(0xFFD87F1C),
                contentColor = Color.White
            ),
                //shadow-border
                border = BorderStroke(
                    3.dp,
                    Brush.verticalGradient(
                        colors = when {
                            !isDarkMode -> listOf(
                                Color(0xFFFFA533),
                                Color(0xFFCC7A00),
                                Color(0xFFB36700)
                            )
                            else -> listOf(Color(0xFFF7B21A), Color(0xFFF1A11A), Color(0xFFDB8F00)
                            )
                        }
                    )
                )) { Text("GUESS") }

            // Info button
            if (currentHintText.isNotBlank()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .border(
                            BorderStroke(
                                width = 3.dp,
                                brush = Brush.verticalGradient(
                                    colors = if (isDarkMode) {
                                        listOf(
                                            Color(0xFFF7B21A),
                                            Color(0xFFF1A11A),
                                            Color(0xFFDB8F00)
                                        )
                                    } else {
                                        listOf(
                                            Color(0xFFFFA533),
                                            Color(0xFFCC7A00),
                                            Color(0xFFB36700)
                                        )
                                    }
                                )
                            ), RoundedCornerShape(12.dp)
                        )
                        .background(
                            if (isDarkMode) MainOrange else Color(0xFFD87F1C),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { showHintPopup(true) }
                ) {
                    Text("i", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }

// Weather button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(50.dp)
                    .border(               BorderStroke(
                        width = 3.dp,
                        brush = Brush.verticalGradient(
                            colors = if (isDarkMode) {
                                listOf(Color(0xFFF7B21A), Color(0xFFF1A11A), Color(0xFFDB8F00))
                            } else {
                                listOf(
                                    Color(0xFFFFA533),
                                    Color(0xFFCC7A00),
                                    Color(0xFFB36700))
                            }
                        )
                    ), RoundedCornerShape(12.dp))
                    .background(if (isDarkMode) MainOrange else Color(0xFFD87F1C), RoundedCornerShape(12.dp))
                    .clickable { showWeatherPopup(true) }
            ) {
                Text("☁", fontSize = 24.sp, color = Color.White)
            }

            Button(onClick = onUseHint, enabled = !hintDisabled, colors = ButtonDefaults.buttonColors(
                containerColor = if (isDarkMode) MainOrange else Color(0xFFD87F1C),
                contentColor = Color.White
            ),
                //shadow-border
                border = BorderStroke(
                    3.dp,
                    Brush.verticalGradient(
                        colors = when {
                            !isDarkMode -> listOf(
                                Color(0xFFFFA533),
                                Color(0xFFCC7A00),
                                Color(0xFFB36700)
                            )
                            else -> listOf(Color(0xFFF7B21A), Color(0xFFF1A11A), Color(0xFFDB8F00)
                            )
                        }
                    )
                )) { Text("HINTS") }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .border(
                    BorderStroke(
                        3.dp,
                        Brush.verticalGradient(
                            colors = when {
                                !isDarkMode -> listOf(
                                    Color(0xFFFFA533),
                                    Color(0xFFCC7A00),
                                    Color(0xFFB36700)
                                )

                                else -> listOf(Color(0xFFF7B21A), Color(0xFFF1A11A), Color(0xFFDB8F00)
                                )
                            }
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(bottom = 12.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                properties = MapProperties(isMyLocationEnabled = hasPermission),
                cameraPositionState = cameraPositionState
            ) {
                if (showBlueCircleOnMap && targetLatLng != null) {
                    val randomCenter = randomCircle(targetLatLng, 200.0)
                    com.google.maps.android.compose.Circle(
                        center = randomCenter,
                        radius = 200.0, // in meters
                        fillColor = Color.Blue.copy(alpha = 0.3f),
                        strokeColor = Color.Blue,
                        strokeWidth = 4f
                    )
                }
            }
        }
    }
}

/**
 * Composable to show a toast message when location permission is not granted.
 *
 * @param locationPermissionToastEvent SharedFlow that emits Boolean values indicating
 * whether to show the toast message.
 */
@Composable
fun NoPermissionInfo(locationPermissionToastEvent: SharedFlow<Boolean>) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        locationPermissionToastEvent.collect { it ->
            if (it) {
                val toast =
                    Toast.makeText(context, "permission locations required", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}

/**
 * Generates a random circle around a target LatLng within a specified maximum radius.
 *
 * @param target The center LatLng
 * @param maxRadiusMeters The maximum radius in meters
 * @return A random LatLng within the circle
 */
fun randomCircle(target: LatLng, maxRadiusMeters: Double): LatLng {
    // Random distance from center (0..maxRadiusMeters)
    val distance = sqrt(Random.nextDouble()) * maxRadiusMeters

    // Random angle in radians
    val angle = Random.nextDouble() * 2 * Math.PI

    // Approximate offsets in degrees
    val offsetLat = distance / 111_000 * sin(angle) // ~111 km per degree latitude
    val offsetLng = distance / (111_000 * cos(Math.toRadians(target.latitude))) * cos(angle)

    return LatLng(target.latitude + offsetLat, target.longitude + offsetLng)
}

/**
 * Composable that displays a weather card with current weather information
 * for the given latitude and longitude.
 *
 * @param lat Latitude of the location
 * @param lon Longitude of the location
 */
@Composable
fun WeatherCard(lat: Double, lon: Double) {
    val viewModel: WeatherViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(lat, lon) {
        viewModel.fetchWeather(lat, lon)
    }

    when (val s = state) {
        is WeatherState.Loading -> {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is WeatherState.Success -> {
            val data = s.data
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Weather at current quest", fontWeight = FontWeight.Bold)
                        Text("Temperature: ${data.current_weather.temperature}°C")
                        Text("Wind: ${data.current_weather.windspeed} m/s")
                        Text("Weather code: ${data.current_weather.weathercode}")
                    }

                    // Right-side icon
                    AsyncImage(
                        model = weatherIcons(data.current_weather.weathercode), // Use your drawable mapping
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        }
        is WeatherState.Error -> {
            Toast.makeText(context, s.message, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }
}

/**
 * Maps weather condition codes to drawable resource IDs.
 *
 * @param code Weather condition code
 * @return Drawable resource ID for the corresponding weather icon
 */
fun weatherIcons(code: Int): Int {
    return when(code) {
        0, 1 -> R.drawable.ic_sunny
        2, 3 -> R.drawable.ic_cloudy
        45, 48 -> R.drawable.ic_fog
        51, 53, 55 -> R.drawable.ic_drizzle
        61, 63 -> R.drawable.ic_rain
        65, 80 -> R.drawable.ic_heavyrain
        71, 73, 75 -> R.drawable.ic_snow
        95 -> R.drawable.ic_thunder
        else -> R.drawable.ic_unknown
    }
}
