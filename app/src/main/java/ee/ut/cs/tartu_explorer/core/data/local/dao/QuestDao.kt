package ee.ut.cs.tartu_explorer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestDao {
    @Query("SELECT * FROM quest WHERE adventureId = :adventureId")
    fun getByAdventure(adventureId: Int): Flow<List<QuestEntity>>
}