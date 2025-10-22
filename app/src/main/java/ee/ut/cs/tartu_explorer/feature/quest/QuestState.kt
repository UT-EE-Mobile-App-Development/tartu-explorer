package ee.ut.cs.tartu_explorer.feature.quest

import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureDifficulty
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.SessionStatus
import ee.ut.cs.tartu_explorer.core.data.repository.AdventureStatusDetails

data class QuestState(
    val adventures: Map<AdventureDifficulty, List<AdventureEntity>> = emptyMap(),
    val adventureStatuses: Map<Long, SessionStatus> = emptyMap(),
    val adventureStatusDetails: Map<Long, AdventureStatusDetails> = emptyMap()
)
