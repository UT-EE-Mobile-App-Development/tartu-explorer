package ee.ut.cs.tartu_explorer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestEntity
import ee.ut.cs.tartu_explorer.core.data.local.relations.AdventureWithQuests
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing operations related to `MapQuestEntity`
 * and its associated `QuestStepEntity` objects within the local database.
 */
@Dao
interface AdventureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdventure(newAdventure: AdventureEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuests(steps: List<QuestEntity>)

    @Transaction
    @Query("SELECT * FROM adventure WHERE id = :adventureId")
    suspend fun getAdventureWithQuests(adventureId: Long): AdventureWithQuests?

    @Transaction
    @Query("SELECT * FROM adventure")
    suspend fun getAllAdventuresWithQuests(): List<AdventureWithQuests>

    @Query("SELECT * FROM adventure")
    fun getAllAdventures(): Flow<List<AdventureEntity>>

    @Query("DELETE FROM adventure")
    suspend fun deleteAllAdventures()

    @Query("DELETE FROM quest")
    suspend fun deleteAllQuests()
}