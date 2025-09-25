package ee.ut.cs.tartu_explorer.feature.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ee.ut.cs.tartu_explorer.R

@Composable
fun GameScreen(onNavigateBack: () -> Unit) {
    // List on Tamp progress bar images
    val images = listOf(
        R.drawable.temp_progress_bar_1,
        R.drawable.temp_progress_bar_2,
        R.drawable.temp_progress_bar_3,
        R.drawable.temp_progress_bar_4,
        R.drawable.temp_progress_bar_5,
        R.drawable.temp_progress_bar_6
    )
    // State to track current image index
    var currentIndex by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }


    // Main Body
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, start = 36.dp, end = 36.dp),
            contentAlignment = Alignment.TopCenter
        ){
            Image(
            painter = painterResource(id = images[currentIndex]),
            contentDescription = "Progress bar",
            )


        }
        // Back Button to go back to "HomeScreen"
        Button(
            onClick = onNavigateBack,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        ) {
            Text("Back")
        }
        // "Hints" Button
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 50.dp, bottom = 120.dp)
        ){
            Text(
                text = "HINTS"
            )
        }
        // Popup if you press the hint button (Temp)
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Hint!") },
                text = { Text("You used a Hint!") },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("OK")}
                }
            )
        }

        // "Guess" Button
        Button(
            onClick = {currentIndex = (currentIndex + 1) % images.size},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 50.dp, bottom = 120.dp)
        ){
            Text(
                text = "GUESS"
            )
        }


        //to Delete
        Text("This is Game Screen")

        // Column to hold picture boxes
        Column(
            verticalArrangement = Arrangement.spacedBy(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // Top "box 1" for Picture
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(200.dp)
                    .border(
                        2.dp,
                        Color.Black,
                        shape = RoundedCornerShape(20.dp)
                    )

            ){
                // Adds Image in "box 1"
                Image(
                    painter = painterResource(id = R.drawable.delta_image),
                    contentDescription = "Picture Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp))
                )

            }
            // Bottom "box 2 for Map
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(200.dp)
                    .border(
                        2.dp,
                        Color.Black,
                        shape = RoundedCornerShape(20.dp)
                        )

            ){
                // Adds the image in "Box 2"
                Image(
                    painter = painterResource(id = R.drawable.delta_maps_image),
                    contentDescription = "Maps Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp))
                )

            }


        }


    }
}