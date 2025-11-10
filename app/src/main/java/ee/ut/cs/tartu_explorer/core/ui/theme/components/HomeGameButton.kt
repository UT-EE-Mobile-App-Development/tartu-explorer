package ee.ut.cs.tartu_explorer.core.ui.theme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Brush
import ee.ut.cs.tartu_explorer.core.ui.theme.MainOrange
import ee.ut.cs.tartu_explorer.core.ui.theme.OrangeGradiantBot
import ee.ut.cs.tartu_explorer.core.ui.theme.OrangeGradiantMid
import ee.ut.cs.tartu_explorer.core.ui.theme.OrangeGradiantTop

@Composable
fun HomeGameButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth(0.6f)
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MainOrange,
            contentColor = Color.Black
        ),
        //shadow-border
        border = BorderStroke(
            3.dp,
            Brush.verticalGradient(
                colors = if(enabled){ //for enabled buttons
                    listOf(
                        OrangeGradiantTop,
                        OrangeGradiantMid,
                        OrangeGradiantBot
                    )
                }else{ //for disabled buttons
                    listOf(
                        Color(0xFFCCCCCC), // light gray top
                        Color(0xFFAAAAAA), // mid gray
                        Color(0xFF888888)  // dark gray bottom
                    )
                }
            )
        )
    ) {
        if(enabled){
            OutlinedText(text = text)
        }
        //for disabled text(homescreen)
        else{
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        }
    }
}
