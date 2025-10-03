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
            containerColor = Color(0xFFFFA500),
            contentColor = Color.Black
        ),
        border = BorderStroke(2.dp, Color.Black)
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
