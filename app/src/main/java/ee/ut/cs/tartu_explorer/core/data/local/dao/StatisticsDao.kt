package ee.ut.cs.tartu_explorer.core.data.local.dao

import androidx.room.*

data class DifficultyCount(
    val difficulty: String, // oder AdventureDifficulty
    val completedQuests: Long
)

data class AvgValue(val valueMs: Double)
data class AvgDouble(val value: Double)

@Dao
interface StatisticsDao {

    // 1) Completed quests per difficulty (counts quest attempts that were successfully completed)
    @Query("""
        SELECT a.difficulty AS difficulty, COUNT(*) AS completedQuests
        FROM quest_attempt qa
        JOIN quest q ON q.id = qa.questId
        JOIN adventure a ON a.id = q.adventureId
        WHERE qa.succeeded = 1 AND EXISTS (
            SELECT 1 FROM adventure_session s
            WHERE s.id = qa.sessionId AND s.playerId = :playerId
        )
        GROUP BY a.difficulty
        ORDER BY a.difficulty
    """)
    suspend fun completedQuestsByDifficulty(playerId: Long): List<DifficultyCount>

    // 2) Total number of hints used
    /*
    @Query("""
        SELECT COUNT(*) FROM hint_usage hu
        JOIN adventure_session s ON s.id = hu.sessionId
        WHERE s.playerId = :playerId
    """)

     */
    //suspend fun totalHintsUsed(playerId: Long): Long

    // 3) Average hints required per quest (average hints per successful attempt)
    /*
    @Query("""
        WITH hints_per_attempt AS (
            SELECT qa.id AS attemptId, COUNT(hu.id) AS hintCount
            FROM quest_attempt qa
            LEFT JOIN hint_usage hu ON hu.attemptId = qa.id
            JOIN adventure_session s ON s.id = qa.sessionId
            WHERE qa.succeeded = 1 AND s.playerId = :playerId
            GROUP BY qa.id
        )
        SELECT AVG(hintCount * 1.0) AS value
        FROM hints_per_attempt
    """)

     */
    //suspend fun avgHintsPerQuest(playerId: Long): AvgDouble?

    // 4) Average time for an adventure (completed sessions only)
    @Query("""
        SELECT AVG((s.completedAt - s.startedAt) * 1.0) AS valueMs
        FROM adventure_session s
        WHERE s.playerId = :playerId AND s.completedAt IS NOT NULL AND s.status = 'COMPLETED'
    """)
    suspend fun avgAdventureDurationMs(playerId: Long): AvgValue?

    // 5) Average time to first hint (per attempt with at least one hint)
    /*
    @Query("""
        WITH first_hint AS (
            SELECT qa.id AS attemptId,
                   MIN(hu.usedAt) - qa.startedAt AS deltaMs
            FROM quest_attempt qa
            JOIN adventure_session s ON s.id = qa.sessionId
            JOIN hint_usage hu ON hu.attemptId = qa.id
            WHERE s.playerId = :playerId
            GROUP BY qa.id
        )
        SELECT AVG(deltaMs * 1.0) AS valueMs
        FROM first_hint
    """)

     */
    //suspend fun avgTimeToFirstHintMs(playerId: Long): AvgValue?
}