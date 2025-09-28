package ee.ut.cs.tartu_explorer.core.data.repository

import ee.ut.cs.tartu_explorer.core.data.local.dao.MapQuestDao
import ee.ut.cs.tartu_explorer.core.data.local.entities.MapQuestEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestStepEntity
import ee.ut.cs.tartu_explorer.core.data.local.relations.MapQuestWithSteps

class MapQuestRepository(private val dao: MapQuestDao) {

    suspend fun insertQuest(quest: MapQuestEntity): Long = dao.insertQuest(quest)

    suspend fun insertSteps(steps: List<QuestStepEntity>) = dao.insertSteps(steps)

    suspend fun getQuestWithSteps(questId: Long): MapQuestWithSteps? = dao.getQuestWithSteps(questId)

    suspend fun getAllQuestsWithSteps(): List<MapQuestWithSteps> = dao.getAllQuestsWithSteps()

    suspend fun deleteAllQuests() = dao.deleteAllQuests()

    suspend fun deleteAllSteps() = dao.deleteAllSteps()
}
