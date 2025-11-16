package ee.ut.cs.tartu_explorer.core.ui.theme.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// Function to add the black outline to white letters
@Composable
fun OutlinedText(
    text: String,
    fontSize: TextUnit? = null,
    fontWeight: FontWeight = FontWeight.Bold,
    textAlign: TextAlign = TextAlign.Start,
    lineHeight: TextUnit = TextUnit.Unspecified,
    textColor: Color,            // top text color
    outlineColor: Color = Color.Black // outline color
) {

    val actualFontSize = fontSize ?: 24.sp
    //so i can have smaller font to

    Box(contentAlignment = Alignment.Center) {
        val offsets = listOf(
            Pair(-1.dp, -1.dp),
            Pair(0.dp, -1.dp),
            Pair(1.dp, -1.dp),
            Pair(-1.dp, 0.dp),
            Pair(1.dp, 0.dp),
            Pair(-1.dp, 1.dp),
            Pair(0.dp, 1.dp),
            Pair(1.dp, 1.dp)
        )

        // Draw black outline layers
        offsets.forEach { (x, y) ->
            Text(
                text = text,
                fontSize = actualFontSize,
                fontWeight = fontWeight,
                color = outlineColor,
                textAlign = textAlign,
                lineHeight = lineHeight,
                modifier = Modifier.offset(x = x, y = y)
            )
        }

        // Draw white top layer
        Text(
            text = text,
            fontSize = actualFontSize,
            fontWeight = fontWeight,
            color = textColor,
            textAlign = textAlign,
            lineHeight = lineHeight
        )
    }
}