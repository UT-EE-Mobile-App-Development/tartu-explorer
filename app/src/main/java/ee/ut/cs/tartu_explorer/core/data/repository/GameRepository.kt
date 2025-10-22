package ee.ut.cs.tartu_explorer.core.data.repository

import ee.ut.cs.tartu_explorer.core.data.local.dao.* // ktlint-disable no-wildcard-imports
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintUsageEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestAttemptEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

data class AdventureStatusDetails(
    val sessionId: Long,
    val status: SessionStatus,
    val totalQuests: Int,
    val completedQuests: Int,
    val hintsUsed: Int,
    val durationMs: Long
)

class GameRepository(
    private val questDao: QuestDao,
    private val hintDao: HintDao,
    private val hintUsageDao: HintUsageDao,
    private val adventureSessionDao: AdventureSessionDao,
    private val questAttemptDao: QuestAttemptDao
) {
    fun getQuestsByAdventure(adventureId: Long) = questDao.getByAdventure(adventureId)
    fun getHintsByQuest(questId: Long) = hintDao.getHint(questId)

    suspend fun trackHintUsed(hintUsage: HintUsageEntity) {
        hintUsageDao.insert(hintUsage)
    }

    suspend fun trackQuestAttempt(questAttempt: QuestAttemptEntity) {
        questAttemptDao.insert(questAttempt)
    }

    fun getAdventureStatusDetails(playerId: Long): Flow<Map<Long, AdventureStatusDetails>> {
        // Triggers that fire whenever the underlying tables change
        val sessionTrigger = adventureSessionDao.getAllSessionsForPlayerAsFlow(playerId)
        val attemptTrigger = questAttemptDao.observeQuestAttemptChanges()
        val hintTrigger = hintUsageDao.observeHintUsageChanges()

        // Combine the triggers. The flatMapLatest will re-execute whenever ANY of them emit a new value.
        return combine(sessionTrigger, attemptTrigger, hintTrigger) { sessions, _, _ ->
            sessions // Pass the sessions through, we only need the trigger
        }.flatMapLatest { sessions ->
            if (sessions.isEmpty()) {
                return@flatMapLatest flowOf(emptyMap())
            }

            val latestSessions = sessions.groupBy { it.adventureId }.mapValues { it.value.first() }
            val adventureIds = latestSessions.keys.toList()
            val sessionIds = latestSessions.values.map { it.id }

            // These flows will now fetch the latest data because this whole block is re-executed
            val questCountsFlow = questDao.getQuestCountsForAdventures(adventureIds)
            val hintCountsFlow = hintUsageDao.getHintCountForSessions(sessionIds)
            val successfulQuestsFlow = questAttemptDao.getSuccessfulAttemptsCountForSessions(sessionIds)

            combine(
                questCountsFlow,
                hintCountsFlow,
                successfulQuestsFlow
            ) { questCounts, hintCounts, successfulQuests ->
                val questCountsMap = questCounts.associateBy({ it.adventureId }, { it.count })
                val hintCountsMap = hintCounts.associateBy({ it.sessionId }, { it.count })
                val successfulQuestsMap = successfulQuests.associateBy({ it.sessionId }, { it.count })

                latestSessions.mapValues { (adventureId, session) ->
                    AdventureStatusDetails(
                        sessionId = session.id,
                        status = session.status,
                        totalQuests = (questCountsMap[adventureId] ?: 0L).toInt(),
                        completedQuests = (successfulQuestsMap[session.id] ?: 0L).toInt(),
                        hintsUsed = (hintCountsMap[session.id] ?: 0L).toInt(),
                        durationMs = if (session.endTime != null) session.endTime!! - session.startTime else System.currentTimeMillis() - session.startTime
                    )
                }
            }
        }
    }
}
