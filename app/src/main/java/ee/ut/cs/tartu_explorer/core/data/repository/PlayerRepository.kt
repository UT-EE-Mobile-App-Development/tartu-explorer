package ee.ut.cs.tartu_explorer.core.data.repository

import ee.ut.cs.tartu_explorer.core.data.local.dao.PlayerDao
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository class for managing player-related operations.
 * @param dao The Data Access Object for PlayerEntity.
 * @property dao The Data Access Object for PlayerEntity.
 * @property getAllPlayers Retrieves a Flow of all PlayerEntity objects.
 * @property insert Inserts a new PlayerEntity into the database.
 */
class PlayerRepository(private val dao: PlayerDao) {
    fun getAllPlayers(): Flow<List<PlayerEntity>> = dao.getAllPlayers()
    suspend fun insert(player: PlayerEntity) = dao.insertPlayer(player)
}
