package ee.ut.cs.tartu_explorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import ee.ut.cs.tartu_explorer.core.ui.theme.TartuExplorerTheme
import ee.ut.cs.tartu_explorer.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TartuExplorerTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    Box (modifier = Modifier.padding(padding)) {
                        AppNavGraph(navController = navController)
                    }
                }
            }
        }
    }
}






