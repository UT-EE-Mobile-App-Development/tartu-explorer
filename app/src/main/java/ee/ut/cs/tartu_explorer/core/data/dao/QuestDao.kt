package ee.ut.cs.tartu_explorer.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import ee.ut.cs.tartu_explorer.core.data.entity.Hint
import ee.ut.cs.tartu_explorer.core.data.entity.Quest
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestDao {

    @Upsert
    suspend fun upsertQuest(quest: Quest)

    @Update
    suspend fun updateQuest(quest: Quest)

    @Delete
    suspend fun deleteQuest(quest: Quest)

    @Query("SELECT * FROM quest WHERE missionId = :missionId")
    fun getQuestByMission(missionId: Int): Flow<List<Quest>>

    @Query("SELECT * FROM hint WHERE hint.questId = :questId")
    fun getHints(questId: Int): Flow<List<Hint>>

    @Query("SELECT * FROM hint")
    fun getHints(): Flow<List<Hint>>
}