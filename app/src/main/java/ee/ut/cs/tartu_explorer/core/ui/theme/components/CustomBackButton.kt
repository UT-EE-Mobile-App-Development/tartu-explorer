package ee.ut.cs.tartu_explorer.core.ui.theme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomBackButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp) // button size
            .border( //custom border, same as on homegamebuttons
                BorderStroke(
                    width = 3.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFFF00),
                            Color(0xFFFFA000),
                            Color(0xFFB35400)
                        )
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .background(color = Color(0xFFFFA500), shape = RoundedCornerShape(8.dp)) // orange fill
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // White arrow with black outline
        Box {
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

            // Draw black outline for arrow
            offsets.forEach { (x, y) ->
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier
                        .offset(x = x, y = y)
                        .size(24.dp)
                )
            }

            // Draw white top arrow
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}