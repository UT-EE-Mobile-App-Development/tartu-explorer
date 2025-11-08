package ee.ut.cs.tartu_explorer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: PlayerEntity): Long

    @Query("SELECT * FROM player WHERE id = :id")
    suspend fun getPlayerById(id: Long): PlayerEntity?

    @Query("SELECT * FROM player")
    fun getAllPlayers(): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM player WHERE isActive = 1")
    suspend fun getActivePlayer(): PlayerEntity?

    @Query("SELECT * FROM player WHERE isActive = 1")
    fun getActivePlayerAsFlow(): Flow<PlayerEntity?>

    @Query("UPDATE player SET isActive = 0 WHERE isActive = 1")
    suspend fun deactivateCurrentPlayer()

    @Query("UPDATE player SET isActive = 1 WHERE id = :playerId")
    suspend fun activatePlayer(playerId: Long)

    @Query("UPDATE player SET experiencePoints = experiencePoints + :points WHERE id = :playerId")
    suspend fun addExperiencePoints(playerId: Long, points: Int)
}
