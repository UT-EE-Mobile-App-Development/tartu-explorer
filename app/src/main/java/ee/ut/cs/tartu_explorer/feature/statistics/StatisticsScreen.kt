package ee.ut.cs.tartu_explorer.feature.statistics

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.ImageLoader
import coil3.asDrawable
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.size.Size
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import ee.ut.cs.tartu_explorer.R
import ee.ut.cs.tartu_explorer.core.data.local.dao.CompletedQuestLocation
import ee.ut.cs.tartu_explorer.core.data.repository.CompletedByDifficulty
import ee.ut.cs.tartu_explorer.core.ui.theme.ThemeViewModel
import ee.ut.cs.tartu_explorer.core.ui.theme.components.AnimatedBackground
import ee.ut.cs.tartu_explorer.core.ui.theme.components.CustomBackButton
import ee.ut.cs.tartu_explorer.core.ui.theme.components.OutlinedText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.min
import kotlin.math.roundToInt

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    vm: StatisticsViewModel = viewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    val backgrounds = listOf(
        R.drawable.bg1,
        R.drawable.bg2
    )

    val themeViewModel: ThemeViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    )
    val isDarkMode by themeViewModel.isDarkMode

    AnimatedBackground(
        backgrounds = backgrounds,
        isDarkMode = isDarkMode
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            OutlinedText(
                                text = "Statistics",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                textColor = Color.White,
                                outlineColor = Color.Black
                            )
                        }
                    },
                    navigationIcon = {
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomBackButton(onClick = onNavigateBack, isDarkMode = isDarkMode)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Transparent),
                    modifier = Modifier.height(96.dp),
                    windowInsets = WindowInsets(0)
                )
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.height(32.dp),
                    windowInsets = WindowInsets(0),
                    containerColor = Transparent
                ) { Text("", modifier = Modifier.padding(horizontal = 8.dp)) }
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (val s = state) {
                    is StatsUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    is StatsUiState.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Error loading statistics: ${s.message}",
                                    textAlign = TextAlign.Center,
                                    color = Color.Black
                                )
                                Spacer(Modifier.height(12.dp))
                                Button(onClick = { vm.refresh() }) {
                                    Text("Try again", color = Color.Black)
                                }
                            }
                        }
                    }

                    is StatsUiState.Loaded -> {
                        val data = s.data
                        var isMapTouched by remember { mutableStateOf(false) }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState(), enabled = !isMapTouched)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            AdventureStatBox(
                                title = "Adventures",
                                started = data.totalAdventuresStarted,
                                finished = data.totalAdventuresFinished,
                                total = data.totalAdventures,
                                color = Color(0xFFF7A71A),
                                gradientColors = listOf(Color(0xFFFFB833), Color(0xFFF7A71A), Color(0xFFE09A00)),
                                isDarkMode = isDarkMode
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                StatBox(
                                    title = "Required hints (total)",
                                    value = data.totalHintsUsed.toString(),
                                    color = Color(0xFFF1A11A),
                                    modifier = Modifier.weight(1f),
                                    gradientColors = listOf(Color(0xFFF7B21A), Color(0xFFF1A11A), Color(0xFFDB8F00)),
                                    isDarkMode = isDarkMode
                                )
                                StatBox(
                                    title = "Ø Hints per quest",
                                    value = formatDoubleOrDash(data.avgHintsPerQuest),
                                    color = Color(0xFFF1A11A),
                                    modifier = Modifier.weight(1f),
                                    gradientColors = listOf(Color(0xFFF7B21A), Color(0xFFF1A11A), Color(0xFFDB8F00)),
                                    isDarkMode = isDarkMode
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                StatBox(
                                    title = "Ø Time for an adventure",
                                    value = formatDurationOrDash(data.avgAdventureDurationMs),
                                    color = Color(0xFFF1A11A),
                                    modifier = Modifier.weight(1f),
                                    gradientColors = listOf(Color(0xFFF7B21A), Color(0xFFF1A11A), Color(0xFFDB8F00)),
                                    isDarkMode = isDarkMode
                                )
                                StatBox(
                                    title = "Ø Time until first hint",
                                    value = formatDurationOrDash(data.avgTimeToFirstHintMs),
                                    color = Color(0xFFF1A11A),
                                    modifier = Modifier.weight(1f),
                                    gradientColors = listOf(Color(0xFFF7B21A), Color(0xFFF1A11A), Color(0xFFDB8F00)),
                                    isDarkMode = isDarkMode
                                )
                            }

                            DifficultyStatBox(
                                title = "Completed Quests by Difficulty",
                                completedByDifficulty = data.completedByDifficulty,
                                color = Color(0xFFE09200),
                                gradientColors = listOf(Color(0xFFF0A200), Color(0xFFE09200), Color(0xFFD08000)),
                                isDarkMode = isDarkMode
                            )

                            // Map of completed quests
                            OutlinedText(
                                text = "Your Completed Quests",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textColor = Color.White,
                                outlineColor = Color.Black
                            )
                            val tartu = LatLng(58.3780, 26.7290)
                            val cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(tartu, 12f)
                            }
                            // Shared shape for border + clipping
                            val borderShape = RoundedCornerShape(16.dp)

                            // Dark mode and light mode gradient borders
                            val borderBrush = if (!isDarkMode) {
                                Brush.verticalGradient(
                                    listOf(
                                        Color.LightGray,
                                        Color.Gray,
                                        Color.DarkGray,
                                    )
                                )
                            } else {
                                Brush.verticalGradient(
                                    listOf(Color(0xFFE59A00), Color(0xFFD08200), Color(0xFFC07000))
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
                                    .border(4.dp, borderBrush, borderShape)
                                    .clip(borderShape)
                                    .pointerInput(Unit) {
                                        awaitPointerEventScope {
                                            while (true) {
                                                val event = awaitPointerEvent()
                                                when (event.type) {
                                                    androidx.compose.ui.input.pointer.PointerEventType.Press -> {
                                                        isMapTouched = true
                                                    }
                                                    androidx.compose.ui.input.pointer.PointerEventType.Release -> {
                                                        isMapTouched = false
                                                    }
                                                }
                                            }
                                        }
                                    }
                            ) {
                                GoogleMap(
                                    modifier = Modifier.fillMaxSize(),
                                    cameraPositionState = cameraPositionState
                                ) {
                                    data.completedQuestLocations.forEach { questLocation ->
                                        QuestMarker(questLocation)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestMarker(questLocation: CompletedQuestLocation) {
    val context = LocalContext.current
    var bitmapDescriptor by remember { mutableStateOf<BitmapDescriptor?>(null) }

    LaunchedEffect(questLocation.hintImageUrl) {
        if (questLocation.hintImageUrl == null) return@LaunchedEffect
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(questLocation.hintImageUrl)
            .size(Size(150, 150)) // Size of Images (Pins)
            .allowHardware(false)
            .build()

        val imageResult = withContext(Dispatchers.IO) {
            loader.execute(request)
        }

        if (imageResult is SuccessResult) {
            // Convert Coil's Image to a Drawable, then to a Bitmap
            val bitmap = imageResult.image.asDrawable(context.resources).toBitmap()
            val croppedBitmap = getCroppedBitmap(bitmap)
            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(croppedBitmap)
        }
    }

    bitmapDescriptor?.let {
        Marker(
            state = rememberMarkerState(position = LatLng(questLocation.latitude, questLocation.longitude)),
            title = "Quest ${questLocation.questId}",
            icon = it
        )
    }
}

fun getCroppedBitmap(bitmap: Bitmap): Bitmap {
    val size = min(bitmap.width, bitmap.height)
    val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(output)
    val paint = Paint()
    val rect = android.graphics.Rect(0, 0, size, size)

    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = -0xbdbdbe
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, rect, rect, paint)

    return output
}

private fun formatDoubleOrDash(value: Double?): String =
    if (value == null) "—" else ((value * 100.0).roundToInt() / 100.0).toString()

private fun formatDurationOrDash(ms: Double?): String {
    if (ms == null) return "—"
    val totalSeconds = (ms / 1000.0).roundToInt()
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return when {
        hours > 0 -> String.format("%dh %02dmin %02ds", hours, minutes, seconds)
        minutes > 0 -> String.format("%dmin %02ds", minutes, seconds)
        else -> String.format("%ds", seconds)
    }
}

private fun mapDifficulty(difficulty: String): String {
    return when (difficulty) {
        "0" -> "Very Easy"
        "1" -> "Easy"
        "2" -> "Medium"
        "3" -> "Hard"
        "4" -> "Very Hard"
        else -> difficulty
    }
}

@Composable
fun AdventureStatBox(
    title: String,
    started: Long,
    finished: Long,
    total: Long,
    color: Color,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean
) {
    val bgColor = if (!isDarkMode) Color(0xFF2C2C2C) else color
    val borderColors = if (!isDarkMode) listOf(Color.LightGray, Color.Gray, Color.DarkGray) else gradientColors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 4.dp,
                brush = Brush.verticalGradient(borderColors),
                shape = RoundedCornerShape(24.dp)
            )
            .background(bgColor, RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = if (!isDarkMode) Color.White else Color.Black
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Started: $started / $total",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = if (!isDarkMode) Color.White else Color.Black
        )
        Text(
            text = "Finished: $finished / $total",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = if (!isDarkMode) Color.White else Color.Black
        )
    }
}


@Composable
fun StatBox(
    title: String, value: String, color: Color, gradientColors: List<Color>, modifier: Modifier = Modifier, isDarkMode: Boolean
) {
    val bgColor = if (!isDarkMode) Color(0xFF2C2C2C) else color
    val borderColors = if (!isDarkMode)
        listOf(Color.LightGray, Color.Gray, Color.DarkGray)
    else
        gradientColors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.3f) // makes each box the correct shape and same size
            .border(
                width = 4.dp,
                brush = Brush.verticalGradient(borderColors),
                shape = RoundedCornerShape(24.dp)
            )
            .background(bgColor, RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = if (!isDarkMode) Color.White else Color.Black
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = if (!isDarkMode) Color.White else Color.Black
        )
    }
}

@Composable
fun DifficultyStatBox(
    title: String,
    completedByDifficulty: List<CompletedByDifficulty>,
    color: Color,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean
) {
    val bgColor = if (!isDarkMode) Color(0xFF2C2C2C) else color
    val borderColors = if (!isDarkMode) listOf(Color.LightGray, Color.Gray, Color.DarkGray) else gradientColors

    val difficultyMap = completedByDifficulty.associate { mapDifficulty(it.difficulty) to it.count }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 4.dp,
                brush = Brush.verticalGradient(borderColors),
                shape = RoundedCornerShape(24.dp)
            )
            .background(bgColor, RoundedCornerShape(24.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = if (!isDarkMode) Color.White else Color.Black
        )
        Spacer(Modifier.height(8.dp))

        if (completedByDifficulty.isEmpty()) {
            Text(
                text = "No completed quests available.",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = if (!isDarkMode) Color.White else Color.Black
            )
        } else {
            val textColor = if (!isDarkMode) Color.White else Color.Black
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Very Easy: ${difficultyMap.getOrDefault("Very Easy", 0L)}", color = textColor)
                    Text("Easy: ${difficultyMap.getOrDefault("Easy", 0L)}", color = textColor)
                    Text("Medium: ${difficultyMap.getOrDefault("Medium", 0L)}", color = textColor)
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Hard: ${difficultyMap.getOrDefault("Hard", 0L)}", color = textColor)
                    Text("Very Hard: ${difficultyMap.getOrDefault("Very Hard", 0L)}", color = textColor)
                }
            }
        }
    }
}
