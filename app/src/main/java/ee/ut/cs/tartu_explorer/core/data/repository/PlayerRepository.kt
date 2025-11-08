package ee.ut.cs.tartu_explorer.core.data.repository

import android.content.Context
import ee.ut.cs.tartu_explorer.core.data.local.dao.PlayerDao
import ee.ut.cs.tartu_explorer.core.data.local.db.DatabaseProvider
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import kotlinx.coroutines.flow.Flow

class PlayerRepository private constructor(private val dao: PlayerDao) {

    /**
     * Retrieves the active player from the database once.
     */
    suspend fun getActivePlayer(): PlayerEntity? = dao.getActivePlayer()

    /**
     * Observes the active player as a Flow, emitting new values upon change.
     */
    fun getActivePlayerAsFlow(): Flow<PlayerEntity?> = dao.getActivePlayerAsFlow()
    
    /**
     * Observes all players in the database.
     */
    fun getAllPlayers(): Flow<List<PlayerEntity>> = dao.getAllPlayers()

    /**
     * Inserts a new player and returns the complete entity with its new ID.
     * If no player is currently active, the new player will be made active.
     */
    suspend fun insertPlayer(player: PlayerEntity): PlayerEntity? {
        val activePlayer = dao.getActivePlayer()
        val playerToInsert = if (activePlayer == null) {
            player.copy(isActive = true)
        } else {
            player.copy(isActive = false)
        }

        val newId = dao.insertPlayer(playerToInsert)
        return dao.getPlayerById(newId)
    }
    
    /**
     * Deactivates the current player and activates the one with the given Id.
     */
    suspend fun switchActivePlayer(playerId: Long) {
        dao.deactivateCurrentPlayer()
        dao.activatePlayer(playerId)
    }

    /**
     * Adds a specified number of experience points to a player's total.
     */
    suspend fun addExperiencePoints(playerId: Long, points: Int) {
        dao.addExperiencePoints(playerId, points)
    }

    companion object {
        @Volatile
        private var INSTANCE: PlayerRepository? = null

        fun from(context: Context): PlayerRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = PlayerRepository(DatabaseProvider.getDatabase(context).playerDao())
                INSTANCE = instance
                instance
            }
        }
    }
}
