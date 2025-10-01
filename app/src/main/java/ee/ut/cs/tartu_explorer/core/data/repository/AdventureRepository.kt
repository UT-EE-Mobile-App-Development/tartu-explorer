package ee.ut.cs.tartu_explorer.core.data.repository

import ee.ut.cs.tartu_explorer.core.data.local.dao.AdventureDao
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestEntity
import ee.ut.cs.tartu_explorer.core.data.local.relations.AdventureWithQuests

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
class AdventureRepository(private val dao: AdventureDao) {

    suspend fun insertQuest(quest: AdventureEntity) = dao.insertAdventure(quest)

    suspend fun insertSteps(steps: List<QuestEntity>) = dao.insertQuests(steps)

    suspend fun getQuestWithSteps(questId: Int): AdventureWithQuests? = dao.getAdventureWithQuests(questId)

    fun getAdventures() = dao.getAllAdventures()

    suspend fun getAllQuestsWithSteps(): List<AdventureWithQuests> = dao.getAllAdventuresWithQuests()

    suspend fun deleteAllQuests() = dao.deleteAllAdventures()

    suspend fun deleteAllSteps() = dao.deleteAllQuests()
}
