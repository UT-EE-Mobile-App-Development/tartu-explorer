package ee.ut.cs.tartu_explorer.feature.quest

import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureDifficulty
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.SessionStatus
import ee.ut.cs.tartu_explorer.core.data.repository.AdventureStatusDetails

/**
 * Represents the state of quests in the quest view model
 *
 * @property adventures Map of adventure difficulties to lists of adventure entities
 * @property adventureStatuses Map of adventure IDs to their session statuses
 * @property adventureStatusDetails Map of adventure IDs to their detailed status information
 */
data class QuestState(
    val adventures: Map<AdventureDifficulty, List<AdventureEntity>> = emptyMap(),
    val adventureStatuses: Map<Long, SessionStatus> = emptyMap(),
    val adventureStatusDetails: Map<Long, AdventureStatusDetails> = emptyMap()
)
