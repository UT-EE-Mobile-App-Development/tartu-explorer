package ee.ut.cs.tartu_explorer.core.data.repository

import ee.ut.cs.tartu_explorer.core.data.local.dao.PlayerDao
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import kotlinx.coroutines.flow.Flow

class PlayerRepository(private val dao: PlayerDao) {
    fun getAllPlayers(): Flow<List<PlayerEntity>> = dao.getAllPlayers()
    suspend fun insert(player: PlayerEntity) = dao.insertPlayer(player)
}
