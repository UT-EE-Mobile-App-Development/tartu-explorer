package ee.ut.cs.tartu_explorer.core.data.repository

import ee.ut.cs.tartu_explorer.core.data.local.dao.MapQuestDao
import ee.ut.cs.tartu_explorer.core.data.local.entities.MapQuestEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestStepEntity
import ee.ut.cs.tartu_explorer.core.data.local.relations.MapQuestWithSteps

/**
 * Repository class for managing MapQuest-related operations.
 * @param dao The Data Access Object for MapQuestEntity and QuestStepEntity.
 * @property dao The Data Access Object for MapQuestEntity and QuestStepEntity.
 * @property insertQuest Inserts a new MapQuestEntity into the database.
 * @property insertSteps Inserts a list of QuestStepEntity into the database.
 * @property getQuestWithSteps Retrieves a MapQuestWithSteps object for a specific quest ID.
 * @property getAllQuestsWithSteps Retrieves a list of MapQuestWithSteps objects for all quests.
 * @property deleteAllQuests Deletes all MapQuestEntity entries from the database.
 * @property deleteAllSteps Deletes all QuestStepEntity entries from the database.
 */
class MapQuestRepository(private val dao: MapQuestDao) {

    suspend fun insertQuest(quest: MapQuestEntity): Long = dao.insertQuest(quest)

    suspend fun insertSteps(steps: List<QuestStepEntity>) = dao.insertSteps(steps)

    suspend fun getQuestWithSteps(questId: Int): MapQuestWithSteps? = dao.getQuestWithSteps(questId)

    suspend fun getAllQuestsWithSteps(): List<MapQuestWithSteps> = dao.getAllQuestsWithSteps()

    suspend fun deleteAllQuests() = dao.deleteAllQuests()

    suspend fun deleteAllSteps() = dao.deleteAllSteps()
}
