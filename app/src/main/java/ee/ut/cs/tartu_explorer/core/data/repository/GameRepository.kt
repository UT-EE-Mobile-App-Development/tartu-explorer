package ee.ut.cs.tartu_explorer.core.data.repository

import ee.ut.cs.tartu_explorer.core.data.local.dao.HintDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.HintUsageDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.QuestDao
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintUsageEntity

class GameRepository(
    private val questDao: QuestDao,
    private val hintDao: HintDao,
    private val hintUsageDao: HintUsageDao
) {
    fun getQuestsByAdventure(adventureId: Long) = questDao.getByAdventure(adventureId)
    fun getHintsByQuest(questId: Long) = hintDao.getHint(questId)

    suspend fun trackHintUsed(hintUsage: HintUsageEntity) {
        hintUsageDao.insert(hintUsage)
    }
}
