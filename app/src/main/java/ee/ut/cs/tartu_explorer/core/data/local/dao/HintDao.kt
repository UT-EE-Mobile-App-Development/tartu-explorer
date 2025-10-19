package ee.ut.cs.tartu_explorer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HintDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hint: HintEntity): Long

    @Upsert
    suspend fun upsertHint(hint: HintEntity)

    @Update
    suspend fun updateHint(hint: HintEntity)

    @Delete
    suspend fun deleteHint(hint: HintEntity)

    @Query("SELECT * FROM hint WHERE hint.questId = :questId")
    fun getHint(questId: Long): Flow<List<HintEntity>>

    @Query("SELECT * FROM hint WHERE hint.questId = :questId AND hint.id = :id")
    fun getHint(questId: Long, id: Int): Flow<HintEntity>
}