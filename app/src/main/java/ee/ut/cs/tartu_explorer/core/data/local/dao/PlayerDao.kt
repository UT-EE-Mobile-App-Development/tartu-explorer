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

    @Query("SELECT * FROM player LIMIT 1")
    suspend fun getFirstPlayer(): PlayerEntity?

    @Query("SELECT * FROM player LIMIT 1")
    fun getPlayerAsFlow(): Flow<PlayerEntity?>

    @Query("UPDATE player SET experiencePoints = experiencePoints + :points WHERE id = :playerId")
    suspend fun addExperiencePoints(playerId: Long, points: Int)
}
