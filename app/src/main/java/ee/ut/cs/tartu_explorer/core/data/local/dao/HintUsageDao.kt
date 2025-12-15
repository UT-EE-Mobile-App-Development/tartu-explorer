package ee.ut.cs.tartu_explorer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintUsageEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data class representing the count of hints used in a specific session.
 *
 * @property sessionId The ID of the session
 * @property count The number of hints used in that session
 */
data class HintCount(val sessionId: Long, val count: Long)

/**
 * Data Access Object (DAO) for managing operations related to `HintUsageEntity`
 * within the local database.
 */
@Dao
interface HintUsageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hintUsage: HintUsageEntity)

    @Query("SELECT COUNT(*) FROM hint_usage WHERE sessionId = :sessionId")
    suspend fun getHintCountForSession(sessionId: Long): Long

    @Query("SELECT sessionId, COUNT(*) as count FROM hint_usage WHERE sessionId IN (:sessionIds) GROUP BY sessionId")
    fun getHintCountForSessions(sessionIds: List<Long>): Flow<List<HintCount>>

    @Query("SELECT MAX(id) FROM hint_usage")
    fun observeHintUsageChanges(): Flow<Long?>
}
