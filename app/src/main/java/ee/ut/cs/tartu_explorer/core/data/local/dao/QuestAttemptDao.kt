package ee.ut.cs.tartu_explorer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestAttemptEntity
import kotlinx.coroutines.flow.Flow

data class SuccessfulAttemptCount(val sessionId: Long, val count: Long)

@Dao
interface QuestAttemptDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attempt: QuestAttemptEntity)

    @Query("SELECT COUNT(*) FROM quest_attempt WHERE sessionId = :sessionId AND wasCorrect = 1")
    suspend fun getSuccessfulAttemptsCountForSession(sessionId: Long): Long

    @Query("SELECT sessionId, COUNT(*) as count FROM quest_attempt WHERE wasCorrect = 1 AND sessionId IN (:sessionIds) GROUP BY sessionId")
    fun getSuccessfulAttemptsCountForSessions(sessionIds: List<Long>): Flow<List<SuccessfulAttemptCount>>

    @Query("SELECT COUNT(*) FROM quest_attempt")
    fun observeQuestAttemptChanges(): Flow<Int>
}
