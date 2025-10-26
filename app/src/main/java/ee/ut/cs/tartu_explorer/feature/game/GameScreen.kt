package ee.ut.cs.tartu_explorer.feature.game

import android.Manifest
import android.R.attr.data
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import ee.ut.cs.tartu_explorer.core.data.local.db.DatabaseProvider
import ee.ut.cs.tartu_explorer.core.data.repository.AdventureSessionRepository
import ee.ut.cs.tartu_explorer.core.data.repository.GameRepository
import ee.ut.cs.tartu_explorer.core.data.repository.PlayerRepository
import ee.ut.cs.tartu_explorer.core.location.LocationRepository
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Popup
import ee.ut.cs.tartu_explorer.R
import ee.ut.cs.tartu_explorer.core.ui.theme.components.AnimatedBackground
import ee.ut.cs.tartu_explorer.core.ui.theme.components.CustomBackButton
import ee.ut.cs.tartu_explorer.feature.weather.WeatherState
import ee.ut.cs.tartu_explorer.feature.weather.WeatherViewModel
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random


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

    val targetLatLng = state.quests.getOrNull(state.currentQuest)?.let { quest ->
        LatLng(quest.latitude, quest.longitude)}


    AnimatedBackground(backgrounds) {
        Box(modifier = Modifier.fillMaxSize()) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                CustomBackButton(onClick = onNavigateBack)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.85f)
                    .align(Alignment.Center)
                    .background(Color(0xCCFFFFFF), RoundedCornerShape(16.dp))
                    .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                        //.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProgressBar(
                        currentStep = state.currentQuest,
                        totalSteps = state.quests.size,
                        modifier = Modifier.fillMaxWidth()
                    )

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
                                val totalHints = 2
                                val hintsUsed = when {
                                    state.currentHint == 0 && !showBlueCircleOnMap -> 0
                                    state.currentHint == 1 && !showBlueCircleOnMap -> 1
                                    showBlueCircleOnMap -> 2
                                    else -> state.currentHint
                                }

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
                                showBlueCircleOnMap = false}},
                        currentHintText = currentHintText,
                        modifier = Modifier.fillMaxWidth(),
                        hintDisabled = state.currentHint >= 2 || showBlueCircleOnMap,
                        showHintPopup = { showHintPopup = it },
                        showWeatherPopup = { showWeatherPopup  = it },
                        showBlueCircleOnMap = showBlueCircleOnMap,
                        targetLatLng = targetLatLng
                    )


                    if (state.guessState != null) {
                        val guess = state.guessState!!
                        DebugGuessDialog(
                            onContinueAnyway = {
                                viewModel.nextQuest()
                                showBlueCircleOnMap = false},
                            onDismiss = { viewModel.resetDebugGuessDialogue() },
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
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = currentHintText,
                            color = Color.Black,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }

            }
            // Centered Hint Popup
            if (showWeatherPopup && targetLatLng != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(32.dp)
                    ) {
                        WeatherCard(lat = targetLatLng.latitude, lon = targetLatLng.longitude)

                    }
                }
            }

        }

    }

}



@Composable
fun ProgressBar(currentStep: Int, totalSteps: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(20.dp)
    ) {
        val progress = currentStep / (totalSteps - 1).toFloat()
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(12.dp)
                .padding(horizontal = 10.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colorStops = arrayOf(
                            progress to Color.Green,
                            progress + 0.01f to Color.White,
                        )
                    )
                )
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            repeat(totalSteps) { index ->
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = if (index < currentStep) Color.Green
                            else if (index == currentStep) Color.Yellow
                            else Color.White,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

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
    targetLatLng: LatLng? = null
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
            Button(onClick = onGuess) { Text("GUESS") }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(50.dp)
                    .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
                    .background(Color.LightGray, RoundedCornerShape(12.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                showHintPopup(true)
                                tryAwaitRelease()
                                showHintPopup(false)
                            }
                        )
                    }
            ) {
                Text("i", color = Color.Black, fontSize = 24.sp)
            }
            // Weather button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(50.dp)
                    .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
                    .background(Color.LightGray, RoundedCornerShape(12.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                showWeatherPopup(true)
                                tryAwaitRelease()
                                showWeatherPopup(false)
                            }
                        )
                    }
            ) {
                Text("☁", fontSize = 24.sp) // icon for weather
            }

            Button(onClick = onUseHint, enabled = !hintDisabled) { Text("HINTS") }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
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

@Composable
fun NoPermissionInfo(locationPermissionToastEvent: SharedFlow<Boolean>) {
    // todo: instead of a toast disable the guess button and show a tooltip
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

// Takes Weather code and chooses correct icon for it
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