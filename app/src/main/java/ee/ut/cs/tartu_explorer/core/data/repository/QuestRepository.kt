package ee.ut.cs.tartu_explorer.core.data.repository

import androidx.room.withTransaction
import ee.ut.cs.tartu_explorer.core.data.local.dao.AdventureDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.PlayerDao
import ee.ut.cs.tartu_explorer.core.data.local.db.AppDatabase
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestEntity
import ee.ut.cs.tartu_explorer.core.data.local.relations.AdventureWithQuests

/**
 * Repository class responsible for managing quest-related operations.
 * Encapsulates data access logic by interacting with DAOs and provides
 * an abstraction layer for handling quests, steps, and player data.
 *
 * @constructor Initializes the repository with the database and relevant DAOs.
 * @param db The application's Room database instance.
 * @param adventureDao Data Access Object for managing quest-related operations.
 * @param playerDao Data Access Object for managing player-related operations.
 */
class QuestRepository(
    private val db: AppDatabase,
    private val adventureDao: AdventureDao,
    private val playerDao: PlayerDao
) {

    suspend fun getAllQuests(): List<AdventureWithQuests> {
        return adventureDao.getAllAdventuresWithQuests()
    }

    suspend fun getQuestWithSteps(questId: Int): AdventureWithQuests? {
        return adventureDao.getAdventureWithQuests(questId)
    }

    suspend fun getPlayer(): PlayerEntity? {
        // WICHTIG: PlayerDao bietet getPlayerById()
        return playerDao.getPlayerById()
    }

    suspend fun insertAdventure(quest: AdventureEntity) {
        return adventureDao.insertAdventure(quest)
    }

    suspend fun insertSteps(steps: List<QuestEntity>) {
        adventureDao.insertQuests(steps)
    }

    suspend fun insertPlayer(player: PlayerEntity) {
        playerDao.insertPlayer(player)
    }

    suspend fun clearDatabase() {
        // Optional: erst Steps, dann Quests
        adventureDao.deleteAllQuests()
        adventureDao.deleteAllAdventures()
        // Optional: Player löschen, falls ihr nicht strikt ID=1 verwendet
        // playerDao.deleteAll()  // nur wenn implementiert
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