package ee.ut.cs.tartu_explorer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ee.ut.cs.tartu_explorer.core.data.local.entities.MapQuestEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestStepEntity
import ee.ut.cs.tartu_explorer.core.data.local.relations.MapQuestWithSteps

/**
 * Data Access Object (DAO) for managing operations related to `MapQuestEntity`
 * and its associated `QuestStepEntity` objects within the local database.
 */
@Dao
interface MapQuestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuest(quest: MapQuestEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: List<QuestStepEntity>)

    @Transaction
    @Query("SELECT * FROM map_quest WHERE id = :questId")
    suspend fun getQuestWithSteps(questId: Int): MapQuestWithSteps?

    @Transaction
    @Query("SELECT * FROM map_quest")
    suspend fun getAllQuestsWithSteps(): List<MapQuestWithSteps>

    @Query("DELETE FROM map_quest")
    suspend fun deleteAllQuests()

    @Query("DELETE FROM quest_steps")
    suspend fun deleteAllSteps()
}