package ee.ut.cs.tartu_explorer.feature.game

import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintEntity

data class GameState(
    val quests: List<QuestEntity> = emptyList(),
    val currentQuest: Int = 0,

    val hints: List<HintEntity> = emptyList(),
    val currentHint: Int = 0,

    val guessState: GuessState? = null
)

data class GuessState(
    val distanceFromTarget: Float,
    val inRange: Boolean
)