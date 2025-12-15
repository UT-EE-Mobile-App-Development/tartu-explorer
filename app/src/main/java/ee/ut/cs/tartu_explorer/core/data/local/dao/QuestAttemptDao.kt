package ee.ut.cs.tartu_explorer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestAttemptEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data class representing the count of successful quest attempts for a specific session.
 *
 * @property sessionId The ID of the session.
 * @property count The count of successful attempts.
 */
data class SuccessfulAttemptCount(val sessionId: Long, val count: Long)

/**
 * Data Access Object (DAO) for managing operations related to `QuestAttemptEntity`
 * within the local database.
 */
@Dao
interface QuestAttemptDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attempt: QuestAttemptEntity)

    @Query("SELECT COUNT(*) FROM quest_attempt WHERE sessionId = :sessionId AND wasCorrect = 1")
    suspend fun getSuccessfulAttemptsCountForSession(sessionId: Long): Long

    @Query("SELECT sessionId, COUNT(DISTINCT questId) as count FROM quest_attempt WHERE wasCorrect = 1 AND sessionId IN (:sessionIds) GROUP BY sessionId")
    fun getSuccessfulAttemptsCountForSessions(sessionIds: List<Long>): Flow<List<SuccessfulAttemptCount>>

    @Query("SELECT * FROM quest_attempt")
    fun observeQuestAttemptChanges(): Flow<List<QuestAttemptEntity>>
}
