package ee.ut.cs.tartu_explorer.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import ee.ut.cs.tartu_explorer.core.data.entity.Mission
import kotlinx.coroutines.flow.Flow

@Dao
interface MissionDao {

    @Upsert
    suspend fun upsertMission(mission: Mission)

    @Update
    suspend fun updateMission(mission: Mission)

    @Delete
    suspend fun deleteMission(mission: Mission)

    @Query("SELECT * FROM mission")
    fun getMission(): Flow<List<Mission>>

    @Query("SELECT * FROM mission WHERE id = :id")
    fun getMission(id: Int): Flow<Mission>
}