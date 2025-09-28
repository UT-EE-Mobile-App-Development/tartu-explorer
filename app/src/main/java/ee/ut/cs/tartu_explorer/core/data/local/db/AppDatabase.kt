package ee.ut.cs.tartu_explorer.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ee.ut.cs.tartu_explorer.core.data.local.dao.MapQuestDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.PlayerDao
import ee.ut.cs.tartu_explorer.core.data.local.entities.MapQuestEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestStepEntity

/**
 * AppDatabase is the Room database for the app, containing the entities `MapQuestEntity`, `QuestStepEntity`, and `PlayerEntity`.
 * It provides DAOs (`MapQuestDao` and `PlayerDao`) to manage quests, steps, and player data, enabling clean and structured access to queries, updates, and transactions.
 */

@Database(
    entities = [
        MapQuestEntity::class,
        QuestStepEntity::class,
        PlayerEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mapQuestDao(): MapQuestDao
    abstract fun playerDao(): PlayerDao
}