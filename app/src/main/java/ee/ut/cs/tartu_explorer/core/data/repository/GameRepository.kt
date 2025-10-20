package ee.ut.cs.tartu_explorer.core.data.repository

import ee.ut.cs.tartu_explorer.core.data.local.dao.AdventureSessionDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.HintDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.HintUsageDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.QuestDao
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintUsageEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.SessionStatus

class GameRepository(
    private val questDao: QuestDao,
    private val hintDao: HintDao,
    private val hintUsageDao: HintUsageDao,
    private val adventureSessionDao: AdventureSessionDao
) {
    fun getQuestsByAdventure(adventureId: Long) = questDao.getByAdventure(adventureId)
    fun getHintsByQuest(questId: Long) = hintDao.getHint(questId)

    suspend fun trackHintUsed(hintUsage: HintUsageEntity) {
        hintUsageDao.insert(hintUsage)
    }

    suspend fun getAdventureStatuses(playerId: Long): Map<Long, SessionStatus> {
        return adventureSessionDao.getAllSessionsForPlayer(playerId)
            .groupBy { it.adventureId }
            .mapValues { (_, sessions) ->
                when {
                    sessions.any { it.status == SessionStatus.COMPLETED } -> SessionStatus.COMPLETED
                    sessions.any { it.status == SessionStatus.IN_PROGRESS } -> SessionStatus.IN_PROGRESS
                    else -> SessionStatus.ABANDONED
                }
            }
    }
}
