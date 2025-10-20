package ee.ut.cs.tartu_explorer.feature.dev

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ee.ut.cs.tartu_explorer.core.ui.theme.components.CustomBackButton

@Composable
fun DevScreen(
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val viewModel: DevViewModel = viewModel(factory = DevViewModelFactory(context.applicationContext))
    val resetOnStart by viewModel.resetOnStart.collectAsState()

    Scaffold(
        topBar = {
            Row(modifier = Modifier.padding(16.dp)) {
                CustomBackButton(onClick = onNavigateBack)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Developer Panel", style = MaterialTheme.typography.headlineMedium)

            Button(onClick = {
                viewModel.onResetDatabaseOnce()
                Toast.makeText(context, "Database will be reset on next app start", Toast.LENGTH_SHORT).show()
            }) {
                Text("Reset & Repopulate Database")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = resetOnStart,
                    onCheckedChange = { viewModel.onToggleResetOnStart(it) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reset database on every app start")
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                "Note: Changes take effect on the next app restart.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
