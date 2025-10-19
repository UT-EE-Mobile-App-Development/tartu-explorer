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

/**
 * Repository class for retrieving and processing statistics-related data.
 * This class is designed to handle data aggregation and computation for player statistics
 * by interacting with the database through the provided `StatisticsDao`.
 *
 * @constructor Private constructor to enforce instantiation through the companion object.
 *
 * @property dao The data access object used for interacting with the statistics-related database tables.
 */
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
        /*
        val totalHintsDeferred = async { dao.totalHintsUsed(playerId) }
        val avgHintsPerQuestDeferred = async { dao.avgHintsPerQuest(playerId)?.value }
        val avgAdventureDurationDeferred = async { dao.avgAdventureDurationMs(playerId)?.valueMs }
        val avgTimeToFirstHintDeferred = async { dao.avgTimeToFirstHintMs(playerId)?.valueMs }

        StatsOverview(
            completedByDifficulty = completedByDiffDeferred.await(),
            totalHintsUsed = totalHintsDeferred.await(),
            avgHintsPerQuest = avgHintsPerQuestDeferred.await(),
            avgAdventureDurationMs = avgAdventureDurationDeferred.await(),
            avgTimeToFirstHintMs = avgTimeToFirstHintDeferred.await()
        )
         */
        StatsOverview(
            completedByDifficulty = emptyList(),
            totalHintsUsed = 0L,
            avgHintsPerQuest = 0.0,
            avgAdventureDurationMs = 0.0,
            avgTimeToFirstHintMs = 0.0
        )
    }
}