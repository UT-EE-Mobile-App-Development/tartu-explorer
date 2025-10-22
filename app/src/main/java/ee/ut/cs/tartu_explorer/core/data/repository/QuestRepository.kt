package ee.ut.cs.tartu_explorer.core.data.repository

import androidx.room.withTransaction
import ee.ut.cs.tartu_explorer.core.data.local.dao.AdventureDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.PlayerDao
import ee.ut.cs.tartu_explorer.core.data.local.db.AppDatabase
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestEntity
import ee.ut.cs.tartu_explorer.core.data.local.relations.AdventureWithQuests

class QuestRepository(
    private val db: AppDatabase,
    private val adventureDao: AdventureDao,
    private val playerDao: PlayerDao
) {

    suspend fun getAllQuests(): List<AdventureWithQuests> {
        return adventureDao.getAllAdventuresWithQuests()
    }

    suspend fun getQuestWithSteps(questId: Long): AdventureWithQuests? {
        return adventureDao.getAdventureWithQuests(questId)
    }

    suspend fun getPlayer(): PlayerEntity? {
        return playerDao.getFirstPlayer()
    }

    suspend fun insertAdventure(quest: AdventureEntity): Long {
        return adventureDao.insertAdventure(quest)
    }

    suspend fun insertSteps(steps: List<QuestEntity>) {
        adventureDao.insertQuests(steps)
    }

    suspend fun insertPlayer(player: PlayerEntity) {
        playerDao.insertPlayer(player)
    }

    suspend fun clearDatabase() {
        adventureDao.deleteAllQuests()
        adventureDao.deleteAllAdventures()
    }

    suspend fun populateDatabaseWithTestData(
        quests: List<AdventureEntity>,
        steps: List<QuestEntity>,
        player: PlayerEntity
    ) {
        db.withTransaction {
            clearDatabase()
            insertPlayer(player)
            quests.forEach { quest -> insertAdventure(quest) }
            insertSteps(steps)
        }
    }
}
