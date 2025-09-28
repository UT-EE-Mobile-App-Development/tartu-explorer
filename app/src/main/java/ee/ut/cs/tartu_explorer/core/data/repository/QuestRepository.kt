package ee.ut.cs.tartu_explorer.core.data.repository

import androidx.room.withTransaction
import ee.ut.cs.tartu_explorer.core.data.local.dao.MapQuestDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.PlayerDao
import ee.ut.cs.tartu_explorer.core.data.local.db.AppDatabase
import ee.ut.cs.tartu_explorer.core.data.local.entities.MapQuestEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestStepEntity
import ee.ut.cs.tartu_explorer.core.data.local.relations.MapQuestWithSteps

class QuestRepository(
    private val db: AppDatabase,
    private val mapQuestDao: MapQuestDao,
    private val playerDao: PlayerDao
) {

    suspend fun getAllQuests(): List<MapQuestWithSteps> {
        return mapQuestDao.getAllQuestsWithSteps()
    }

    suspend fun getQuestWithSteps(questId: Int): MapQuestWithSteps? {
        return mapQuestDao.getQuestWithSteps(questId)
    }

    suspend fun getPlayer(): PlayerEntity? {
        // WICHTIG: PlayerDao bietet getPlayerById()
        return playerDao.getPlayerById()
    }

    suspend fun insertQuest(quest: MapQuestEntity): Long {
        return mapQuestDao.insertQuest(quest)
    }

    suspend fun insertSteps(steps: List<QuestStepEntity>) {
        mapQuestDao.insertSteps(steps)
    }

    suspend fun insertPlayer(player: PlayerEntity) {
        playerDao.insertPlayer(player)
    }

    suspend fun clearDatabase() {
        // Optional: erst Steps, dann Quests
        mapQuestDao.deleteAllSteps()
        mapQuestDao.deleteAllQuests()
        // Optional: Player löschen, falls ihr nicht strikt ID=1 verwendet
        // playerDao.deleteAll()  // nur wenn implementiert
    }

    suspend fun populateDatabaseWithTestData(
        quests: List<MapQuestEntity>,
        steps: List<QuestStepEntity>,
        player: PlayerEntity
    ) {
        db.withTransaction {
            clearDatabase()
            insertPlayer(player)
            quests.forEach { quest -> insertQuest(quest) }
            insertSteps(steps)
        }
    }
}