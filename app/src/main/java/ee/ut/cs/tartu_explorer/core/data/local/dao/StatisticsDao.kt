package ee.ut.cs.tartu_explorer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query

data class DifficultyCount(
    val difficulty: String, // oder AdventureDifficulty
    val completedQuests: Long
)

data class AvgValue(val valueMs: Double?)
data class AvgDouble(val value: Double?)

data class CompletedQuestLocation(
    val latitude: Double,
    val longitude: Double,
    // QuestEntity doesn't have a thumbnail, so we'll use the adventure thumbnail.
    // We'll also need the quest ID if we want to show quest-specific images later.
    val questId: Long,
    val adventureThumbnailPath: String
)

@Dao
interface StatisticsDao {

    // 1) Completed quests per difficulty
    @Query("""
        SELECT a.difficulty AS difficulty, COUNT(*) AS completedQuests
        FROM quest_attempt qa
        JOIN quest q ON q.id = qa.questId
        JOIN adventure a ON a.id = q.adventureId
        WHERE qa.wasCorrect = 1 AND EXISTS (
            SELECT 1 FROM adventure_session s
            WHERE s.id = qa.sessionId AND s.playerId = :playerId
        )
        GROUP BY a.difficulty
        ORDER BY a.difficulty
    """)
    suspend fun completedQuestsByDifficulty(playerId: Long): List<DifficultyCount>

    // 2) Total number of hints used
    @Query("""
        SELECT COUNT(*) FROM hint_usage hu
        JOIN adventure_session s ON s.id = hu.sessionId
        WHERE s.playerId = :playerId
    """)
    suspend fun totalHintsUsed(playerId: Long): Long

    // 3) Total successful quest attempts
    @Query("""
        SELECT COUNT(*) FROM quest_attempt
        WHERE wasCorrect = 1 AND sessionId IN (
            SELECT id FROM adventure_session WHERE playerId = :playerId
        )
    """)
    suspend fun totalSuccessfulQuestAttempts(playerId: Long): Long

    // 4) Average time for an adventure (completed sessions only)
    @Query("""
        SELECT AVG((s.endTime - s.startTime) * 1.0) AS valueMs
        FROM adventure_session s
        WHERE s.playerId = :playerId AND s.endTime IS NOT NULL AND s.status = 'COMPLETED'
    """)
    suspend fun avgAdventureDurationMs(playerId: Long): AvgValue?

    // 5) Average time to first hint (per session with at least one hint)
    @Query("""
        WITH time_to_first_hint_per_session AS (
            SELECT MIN(hu.usedAt) - s.startTime as deltaMs
            FROM adventure_session s
            JOIN hint_usage hu ON s.id = hu.sessionId
            WHERE s.playerId = :playerId
            GROUP BY s.id, s.startTime
            HAVING COUNT(hu.id) > 0
        )
        SELECT AVG(deltaMs * 1.0) as valueMs
        FROM time_to_first_hint_per_session
    """)
    suspend fun avgTimeToFirstHintMs(playerId: Long): AvgValue?

    // 6) Locations of all successfully completed quests for the map
    @Query("""
        SELECT DISTINCT q.id AS questId, q.latitude, q.longitude, a.thumbnailPath as adventureThumbnailPath
        FROM quest_attempt qa
        JOIN quest q ON q.id = qa.questId
        JOIN adventure a ON a.id = q.adventureId
        WHERE qa.wasCorrect = 1 AND qa.sessionId IN (
            SELECT id FROM adventure_session WHERE playerId = :playerId
        )
    """)
    suspend fun getCompletedQuestLocations(playerId: Long): List<CompletedQuestLocation>
}
