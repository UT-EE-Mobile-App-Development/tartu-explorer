package ee.ut.cs.tartu_explorer.feature.quest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
//Quests Screen
fun QuestScreen(onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Button(
            onClick = onNavigateBack,
            modifier = Modifier.align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
            ) {
            Text("Back")
        }
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
                    Row(){
                        Text("picture")
                        Text("picture")
                        Text("picture")
                    }
                }
            }

            //medium difficulty
            Row(){
                Column(){
                    Text("Medium")
                    //pictures for medium difficulty
                    Row(){
                        Text("picture")
                        Text("picture")
                        Text("picture")
                    }
                }
            }

            //hard difficulty
            Row(){
                Column(){
                    Text("Hard")
                    //pictures for hard difficulty
                    Row(){
                        Text("picture")
                        Text("picture")
                        Text("picture")
                    }
                }
            }
        }
    }

}