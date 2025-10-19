package ee.ut.cs.tartu_explorer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintUsageEntity

@Dao
interface HintUsageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hintUsage: HintUsageEntity)
}
