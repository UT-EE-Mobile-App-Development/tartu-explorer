package ee.ut.cs.tartu_explorer.core.data.repository

import android.content.Context
import ee.ut.cs.tartu_explorer.core.data.local.dao.PlayerDao
import ee.ut.cs.tartu_explorer.core.data.local.db.DatabaseProvider
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository class for managing player-related operations.
 * @param dao The Data Access Object for PlayerEntity.
 */
class PlayerRepository private constructor(private val dao: PlayerDao) {
    fun getPlayer(): Flow<PlayerEntity?> = dao.getPlayer()
    fun getAllPlayers(): Flow<List<PlayerEntity>> = dao.getAllPlayers()
    suspend fun insertPlayer(player: PlayerEntity) = dao.insertPlayer(player)

    companion object {
        fun from(context: Context): PlayerRepository {
            val db = DatabaseProvider.getDatabase(context)
            return PlayerRepository(db.playerDao())
        }
    }
}
