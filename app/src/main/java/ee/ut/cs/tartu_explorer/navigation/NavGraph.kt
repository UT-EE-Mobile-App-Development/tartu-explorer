package ee.ut.cs.tartu_explorer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ee.ut.cs.tartu_explorer.feature.game.GameScreen
import ee.ut.cs.tartu_explorer.feature.home.HomeScreen
import ee.ut.cs.tartu_explorer.feature.quest.QuestScreen
import ee.ut.cs.tartu_explorer.feature.statistics.StatisticsScreen
import kotlinx.serialization.Serializable


//routs to screens
@Serializable
sealed class Screen(val route: String) {
    @Serializable
    data class Home(val selectedQuest:Int? = null): Screen("home")
    object Quest : Screen("quest")
    object Statistics : Screen("statistics")
    object Game : Screen("game")
}


@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home()) {

        //Home Screen button navigation
        composable<Screen.Home> { backStackEntry ->
            val home: Screen.Home = backStackEntry.toRoute()
            HomeScreen(
                selectedAdventureId = home.selectedQuest,
                onNavigateToQuest = { navController.navigate(Screen.Quest.route) },
                onNavigateToStatistics = { navController.navigate(Screen.Statistics.route) },
                onNavigateToGame = { navController.navigate(Screen.Game.route) }
            )
        }
        //Quests Screen button navigation
        composable(Screen.Quest.route) {
            QuestScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateHome = { adventureId:Int -> navController.navigate(Screen.Home(adventureId)) }
            )
        }

        //Statistics Screen button navigation
        composable(Screen.Statistics.route) {
            StatisticsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        //Game Screen button navigation
        composable(Screen.Game.route) {
            GameScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

    }
}

