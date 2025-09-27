package ee.ut.cs.tartu_explorer.feature.game

import ee.ut.cs.tartu_explorer.core.data.entity.Hint
import ee.ut.cs.tartu_explorer.core.data.entity.Mission
import ee.ut.cs.tartu_explorer.core.data.entity.Quest

data class QuestState(
    val mission: Mission? = null,
    val quests: List<Quest> = emptyList(),
    val currentQuest: Int = 1,

    // content inside quest
    val hints: List<Hint> = emptyList(),
    val currentHint: Int = 1,
)