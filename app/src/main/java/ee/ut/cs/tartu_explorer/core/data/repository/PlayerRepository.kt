package ee.ut.cs.tartu_explorer.core.data.repository

import android.content.Context
import ee.ut.cs.tartu_explorer.core.data.local.dao.PlayerDao
import ee.ut.cs.tartu_explorer.core.data.local.db.DatabaseProvider
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import kotlinx.coroutines.flow.Flow

class PlayerRepository private constructor(private val dao: PlayerDao) {

    /**
     * Retrieves the first available player from the database once.
     */
    suspend fun getFirstPlayer(): PlayerEntity? = dao.getFirstPlayer()

    /**
     * Observes the first available player as a Flow, emitting new values upon change.
     */
    fun getPlayerAsFlow(): Flow<PlayerEntity?> = dao.getPlayerAsFlow()

    /**
     * Inserts a new player and returns the complete entity with its new ID.
     */
    suspend fun insertPlayer(player: PlayerEntity): PlayerEntity? {
        val newId = dao.insertPlayer(player)
        return dao.getPlayerById(newId)
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
