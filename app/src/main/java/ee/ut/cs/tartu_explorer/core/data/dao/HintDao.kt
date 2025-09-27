package ee.ut.cs.tartu_explorer.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import ee.ut.cs.tartu_explorer.core.data.entity.Hint
import kotlinx.coroutines.flow.Flow

@Dao
interface HintDao {

    @Upsert
    suspend fun upsertHint(hint: Hint)

    @Update
    suspend fun updateHint(hint: Hint)

    @Delete
    suspend fun deleteHint(hint: Hint)

    @Query("SELECT * FROM hint WHERE hint.questId = :questId")
    fun getHint(questId: Int): Flow<List<Hint>>

    @Query("SELECT * FROM hint WHERE hint.questId = :questId and `index` = :id")
    fun getHint(questId: Int, id: Int): Flow<Hint>
}