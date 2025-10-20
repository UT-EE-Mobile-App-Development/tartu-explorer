package ee.ut.cs.tartu_explorer.feature.quest

import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureDifficulty
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.SessionStatus

data class QuestState(
    val adventures: Map<AdventureDifficulty, List<AdventureEntity>> = mapOf(
        AdventureDifficulty.VERY_EASY to emptyList(),
        AdventureDifficulty.EASY to emptyList(),
        AdventureDifficulty.MEDIUM to emptyList(),
        AdventureDifficulty.HARD to emptyList(),
        AdventureDifficulty.VERY_HARD to emptyList(),
    ),
    val adventureStatuses: Map<Long, SessionStatus> = emptyMap()
)
