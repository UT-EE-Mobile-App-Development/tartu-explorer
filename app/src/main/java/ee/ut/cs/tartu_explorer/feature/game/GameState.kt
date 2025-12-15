package ee.ut.cs.tartu_explorer.feature.game

import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintEntity

/**
 * Represents the game state used in the game view model
 *
 * @property quests List of quests in the game
 * @property currentQuest Index of the current quest
 * @property hints List of hints available in the game
 * @property currentHint Index of the current hint
 * @property guessState State of the user's guess, if any
 */
data class GameState(
    val quests: List<QuestEntity> = emptyList(),
    val currentQuest: Int = 0,

    val hints: List<HintEntity> = emptyList(),
    val currentHint: Int = 0,

    val guessState: GuessState? = null
)

/**
 * Represents the state of a user's guess in the game.
 *
 * @property distanceFromTarget Distance from the guessed location to the target location in meters.
 * @property inRange Boolean whether the guess is within the accepted range of the target.
 */
data class GuessState(
    val distanceFromTarget: Float,
    val inRange: Boolean
)