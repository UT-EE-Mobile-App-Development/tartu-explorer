package ee.ut.cs.tartu_explorer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureSessionEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.SessionStatus

@Dao
interface AdventureSessionDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertSession(session: AdventureSessionEntity): Long

    @Query("SELECT * FROM adventure_session WHERE id = :sessionId")
    suspend fun getSession(sessionId: Long): AdventureSessionEntity?

    @Query("SELECT * FROM adventure_session WHERE adventureId = :adventureId AND playerId = :playerId AND status = 'IN_PROGRESS' ORDER BY startTime DESC LIMIT 1")
    suspend fun getActiveSession(adventureId: Long, playerId: Long): AdventureSessionEntity?

    @Query("UPDATE adventure_session SET endTime = :endTime, status = :status WHERE id = :sessionId")
    suspend fun updateSessionStatus(sessionId: Long, endTime: Long?, status: SessionStatus)

    @Query("UPDATE adventure_session SET currentQuestIndex = :questIndex, currentHintIndex = :hintIndex WHERE id = :sessionId")
    suspend fun updateSessionProgress(sessionId: Long, questIndex: Int, hintIndex: Int)

    @Query("SELECT * FROM adventure_session WHERE playerId = :playerId")
    suspend fun getAllSessionsForPlayer(playerId: Long): List<AdventureSessionEntity>
}