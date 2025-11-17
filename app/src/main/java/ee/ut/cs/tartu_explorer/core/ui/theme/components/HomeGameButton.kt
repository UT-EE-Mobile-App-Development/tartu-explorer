package ee.ut.cs.tartu_explorer.core.ui.theme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ee.ut.cs.tartu_explorer.core.ui.theme.MainOrange
import ee.ut.cs.tartu_explorer.core.ui.theme.OrangeGradiantBot
import ee.ut.cs.tartu_explorer.core.ui.theme.OrangeGradiantMid
import ee.ut.cs.tartu_explorer.core.ui.theme.OrangeGradiantTop

@Composable
fun HomeGameButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    isDarkMode: Boolean = false,
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
            containerColor = if (isDarkMode) MainOrange else Color(0xFFD87F1C),
            contentColor = if (isDarkMode) Color.Black else Color.DarkGray
        ),
        //shadow-border
        border = BorderStroke(
            3.dp,
            Brush.verticalGradient(
                colors = when {
                    !enabled -> listOf(
                        Color(0xFFCCCCCC), // light gray top
                        Color(0xFFAAAAAA), // mid gray
                        Color(0xFF888888)  // dark gray bottom
                    )

                    !isDarkMode -> listOf(
                        Color(0xFFFFA533),
                        Color(0xFFCC7A00),
                        Color(0xFFB36700)
                    )

                    else -> listOf(
                        OrangeGradiantTop,
                        OrangeGradiantMid,
                        OrangeGradiantBot
                    )
                }
            )
        )
    ) {
        if (enabled) {
            OutlinedText(
                text = text,
                textColor = if (isDarkMode) Color.White else Color.White,
                outlineColor = if (isDarkMode) Color.Black else Color.Black
            )
        } else {
            OutlinedText(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textColor = Color.White,
                outlineColor = Color.Black
            )
        }
    }
}
