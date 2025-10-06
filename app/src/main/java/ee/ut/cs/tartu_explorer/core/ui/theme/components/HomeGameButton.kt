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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush

@Composable
fun HomeGameButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val borderColor = if (enabled) Color(0xFF8B4500) else Color.Gray



    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth(0.6f)
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFFA500),
            contentColor = Color.Black
        ),
        //border
        border = BorderStroke(
            3.dp,
            Brush.verticalGradient(

                colors = if(enabled){
                    listOf(
                        Color(0xFFFFE0A0), // light top highlight
                        Color(0xFFFFA000), // mid-orange
                        Color(0xFF8B3A00)  // dark bottom shadow
                    )
                }else{
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
        //for not enabled text(homescreen)
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
