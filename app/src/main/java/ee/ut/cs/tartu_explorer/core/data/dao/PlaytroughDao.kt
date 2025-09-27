package ee.ut.cs.tartu_explorer.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import ee.ut.cs.tartu_explorer.core.data.entity.Playtrough
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaytroughDao {

    @Upsert
    suspend fun upsertPlaytrough(playtrough: Playtrough)

    @Update
    suspend fun updatePlaytrough(playtrough: Playtrough)

    @Delete
    suspend fun deletePlaytrough(playtrough: Playtrough)

    @Query("SELECT * FROM playtrough WHERE active = :active")
    fun getPlaytrough(active: Boolean): Flow<List<Playtrough>>

    @Query("SELECT * FROM playtrough WHERE id = :id")
    fun getPlaytrough(id: Int): Flow<Playtrough>
}