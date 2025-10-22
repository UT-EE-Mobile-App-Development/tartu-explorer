package ee.ut.cs.tartu_explorer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestEntity
import kotlinx.coroutines.flow.Flow

// Data class to hold the result of the COUNT query. This is a workaround for a Room KSP bug.
data class QuestCount(val adventureId: Long, val count: Long)

@Dao
interface QuestDao {
    @Query("SELECT * FROM quest WHERE adventureId = :adventureId")
    fun getByAdventure(adventureId: Long): Flow<List<QuestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quest: QuestEntity): Long

    @Query("SELECT adventureId, COUNT(*) as count FROM quest WHERE adventureId IN (:adventureIds) GROUP BY adventureId")
    fun getQuestCountsForAdventures(adventureIds: List<Long>): Flow<List<QuestCount>>
}
