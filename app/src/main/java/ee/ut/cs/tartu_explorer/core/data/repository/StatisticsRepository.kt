package ee.ut.cs.tartu_explorer.core.data.repository

import ee.ut.cs.tartu_explorer.core.data.local.dao.StatisticsDao
import ee.ut.cs.tartu_explorer.core.data.local.db.DatabaseProvider
import android.content.Context
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

data class CompletedByDifficulty(
    val difficulty: String,
    val count: Long
)

data class StatsOverview(
    val completedByDifficulty: List<CompletedByDifficulty>,
    val totalHintsUsed: Long,
    val avgHintsPerQuest: Double?,
    val avgAdventureDurationMs: Double?,
    val avgTimeToFirstHintMs: Double?
)

class StatisticsRepository private constructor(
    private val dao: StatisticsDao
) {
    companion object {
        fun from(context: Context): StatisticsRepository {
            val db = DatabaseProvider.getDatabase(context)
            return StatisticsRepository(db.statisticsDao())
        }
    }

    suspend fun loadOverview(playerId: Long): StatsOverview = coroutineScope {
        val completedByDiffDeferred = async {
            dao.completedQuestsByDifficulty(playerId).map {
                CompletedByDifficulty(difficulty = it.difficulty, count = it.completedQuests)
            }
        }
        val totalHintsDeferred = async { dao.totalHintsUsed(playerId) }
        val totalSuccessfulQuestsDeferred = async { dao.totalSuccessfulQuestAttempts(playerId) }
        val avgAdventureDurationDeferred = async { dao.avgAdventureDurationMs(playerId)?.valueMs }
        val avgTimeToFirstHintDeferred = async { dao.avgTimeToFirstHintMs(playerId)?.valueMs }

        val totalHints = totalHintsDeferred.await()
        val totalSuccessfulQuests = totalSuccessfulQuestsDeferred.await()

        val avgHintsPerQuest = if (totalSuccessfulQuests > 0) {
            totalHints.toDouble() / totalSuccessfulQuests
        } else {
            null
        }

        StatsOverview(
            completedByDifficulty = completedByDiffDeferred.await(),
            totalHintsUsed = totalHints,
            avgHintsPerQuest = avgHintsPerQuest,
            avgAdventureDurationMs = avgAdventureDurationDeferred.await(),
            avgTimeToFirstHintMs = avgTimeToFirstHintDeferred.await()
        )
    }
}