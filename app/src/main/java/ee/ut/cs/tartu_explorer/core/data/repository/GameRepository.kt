package ee.ut.cs.tartu_explorer.core.data.repository

import ee.ut.cs.tartu_explorer.core.data.local.dao.HintDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.QuestDao

class GameRepository(private val questDao: QuestDao, private val hintDao: HintDao) {
    fun getQuestsByAdventure(adventureId: Int) = questDao.getByAdventure(adventureId)
    fun getHintsByQuest(questId: Int) = hintDao.getHint(questId)
}
