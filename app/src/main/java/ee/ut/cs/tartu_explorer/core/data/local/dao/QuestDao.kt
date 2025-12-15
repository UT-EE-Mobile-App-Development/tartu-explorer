package ee.ut.cs.tartu_explorer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data class representing the count of quests for a specific adventure.
 * It holds the result of the COUNT query. This is a workaround for a Room KSP bug.
 *
 * @property adventureId The ID of the adventure.
 * @property count The number of quests associated with the adventure.
 */
data class QuestCount(val adventureId: Long, val count: Long)

/**
 * Data Access Object (DAO) for managing operations related to `QuestEntity`
 * within the local database.
 */
@Dao
interface QuestDao {
    @Query("SELECT * FROM quest WHERE adventureId = :adventureId")
    fun getByAdventure(adventureId: Long): Flow<List<QuestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quest: QuestEntity): Long

    @Query("SELECT adventureId, COUNT(*) as count FROM quest WHERE adventureId IN (:adventureIds) GROUP BY adventureId")
    fun getQuestCountsForAdventures(adventureIds: List<Long>): Flow<List<QuestCount>>
}
