package ee.ut.cs.tartu_explorer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import kotlinx.coroutines.flow.Flow

private const val DEFAULT_PLAYER_ID: Int = 1

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: PlayerEntity): Long

    @Update
    suspend fun updatePlayer(player: PlayerEntity)

    @Query("SELECT * FROM player WHERE id = :id")
    suspend fun getPlayerById(id: Int = DEFAULT_PLAYER_ID): PlayerEntity?

    @Query("SELECT * FROM player WHERE id = :id")
    fun observePlayerById(id: Int = DEFAULT_PLAYER_ID): Flow<PlayerEntity?>

    @Query("SELECT * FROM player")
    fun getAllPlayers(): Flow<List<PlayerEntity>>
}