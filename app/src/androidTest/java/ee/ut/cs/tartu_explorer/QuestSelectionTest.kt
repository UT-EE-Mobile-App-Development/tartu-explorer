package ee.ut.cs.tartu_explorer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import ee.ut.cs.tartu_explorer.core.ui.theme.TartuExplorerTheme
import ee.ut.cs.tartu_explorer.navigation.AppNavGraph
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuestSelectionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun selectDemoQuest() {
        composeTestRule.setContent {
            TartuExplorerTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    Box (modifier = Modifier.padding(padding)) {
                        AppNavGraph(navController = navController)
                    }
                }
            }
        }

        // registration
        if (composeTestRule.onAllNodesWithText("Please enter your name to begin.").fetchSemanticsNodes().size == 1) {
            composeTestRule.onNodeWithText("Your Name").performTextInput("Test User")
            composeTestRule.onNodeWithText("Save").performClick()
        }

        composeTestRule.onNodeWithText("Select an adventure to play").assertIsDisplayed()
        composeTestRule.onNodeWithText("QUESTS").performClick()
        composeTestRule.onNodeWithText("Easy").performClick()

        // Wait for the content to appear (due to animation or async DB loading)
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithContentDescription("Riverside-Walk").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Riverside-Walk").performClick()
        composeTestRule.waitUntil(timeoutMillis = 30000) {
            // wait for image download to finish or max 30 seconds
            composeTestRule.onAllNodesWithText("PLAY").fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithText("PLAY").assertIsDisplayed()
        composeTestRule.onNodeWithText("PLAY").performClick()
    }

}