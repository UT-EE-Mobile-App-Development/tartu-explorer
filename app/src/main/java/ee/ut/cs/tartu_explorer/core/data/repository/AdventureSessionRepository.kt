package ee.ut.cs.tartu_explorer.core.data.repository

import ee.ut.cs.tartu_explorer.core.data.local.db.AdventureSessionDao
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureSessionEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.SessionStatus

class AdventureSessionRepository(
    private val adventureSessionDao: AdventureSessionDao
) {
    suspend fun startNewSession(adventureId: Long, playerId: Long): Long {
        val session = AdventureSessionEntity(adventureId = adventureId, playerId = playerId)
        return adventureSessionDao.insertSession(session)
    }

    suspend fun getActiveSession(adventureId: Long, playerId: Long) = adventureSessionDao.getActiveSession(adventureId, playerId)

    suspend fun completeSession(sessionId: Long) {
        adventureSessionDao.updateSessionStatus(sessionId, System.currentTimeMillis(), SessionStatus.COMPLETED)
    }

    suspend fun abandonSession(sessionId: Long) {
        adventureSessionDao.updateSessionStatus(sessionId, System.currentTimeMillis(), SessionStatus.ABANDONED)
    }

    suspend fun updateProgress(sessionId: Long, questIndex: Int, hintIndex: Int) {
        adventureSessionDao.updateSessionProgress(sessionId, questIndex, hintIndex)
    }

    suspend fun getAllSessionsForPlayer(playerId: Long) = adventureSessionDao.getAllSessionsForPlayer(playerId)


}
