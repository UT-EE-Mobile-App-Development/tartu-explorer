package ee.ut.cs.tartu_explorer.feature.quest
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ee.ut.cs.tartu_explorer.R

@Composable
//Quests Screen
fun QuestScreen(onNavigateBack: () -> Unit) {
    //to be replaced with a function that gets the data from the backend someday
    val easyLevelsPictures = listOf(
        R.drawable.temp_progress_bar_1,
        R.drawable.temp_progress_bar_2,
        R.drawable.temp_progress_bar_3,
        R.drawable.temp_progress_bar_4,
        R.drawable.temp_progress_bar_5,
        R.drawable.temp_progress_bar_6
    )

    //to be replaced with a function that gets the data from the backend someday
    val mediumLevelsPictures = listOf(
        R.drawable.temp_progress_bar_1,
        R.drawable.temp_progress_bar_2,
        R.drawable.temp_progress_bar_3,
        R.drawable.temp_progress_bar_4,
        R.drawable.temp_progress_bar_5,
        R.drawable.temp_progress_bar_6
    )

    //to be replaced with a function that gets the data from the backend someday
    val hardLevelsPictures = listOf(
        R.drawable.temp_progress_bar_1,
        R.drawable.temp_progress_bar_2,
        R.drawable.temp_progress_bar_3,
        R.drawable.temp_progress_bar_4,
        R.drawable.temp_progress_bar_5,
        R.drawable.temp_progress_bar_6
    )

    //scroll states for the pictures on different difficulties
    val easyScrollState = rememberScrollState()
    val mediumScrollState = rememberScrollState()
    val hardScrollState = rememberScrollState()


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        //to Delete
        Text("This is Quests Screen")

        //for Quest difficulties
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = 20.dp,
                    top = 70.dp
                )
        ){

            //easy difficulty
            Row(){
                Column(){
                    Text("Easy")

                    //pictures for easy difficulty
                    Row(
                        modifier = Modifier.horizontalScroll(easyScrollState) // enables scrolling
                    ){
                        //displays each picture from easyLevelsPictures
                        easyLevelsPictures.forEach { picture ->
                            Image(
                                painter = painterResource(id = picture),
                                contentDescription = "Easy levels",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }

            //medium difficulty
            Row(){
                Column(){
                    Text("Medium")
                    //pictures for medium difficulty
                    Row(
                        modifier = Modifier.horizontalScroll(mediumScrollState) // enables scrolling
                    ){
                        //displays each picture from mediumLevelsPictures
                        mediumLevelsPictures.forEach { picture ->
                            Image(
                                painter = painterResource(id = picture),
                                contentDescription = "Easy levels",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }

            //hard difficulty
            Row(){
                Column(){
                    Text("Hard")
                    //pictures for hard difficulty
                    Row(
                        modifier = Modifier.horizontalScroll(hardScrollState) // enables scrolling
                    ){
                        //displays each picture from hardLevelsPictures
                        hardLevelsPictures.forEach { picture ->
                            Image(
                                painter = painterResource(id = picture),
                                contentDescription = "Easy levels",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = onNavigateBack,
            modifier = Modifier.align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        ) {
            Text("Back")
        }
    }

}