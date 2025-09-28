package ee.ut.cs.tartu_explorer.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ee.ut.cs.tartu_explorer.core.data.local.dao.AdventureDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.PlayerDao
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestEntity

/**
 * AppDatabase is the Room database for the app, containing the entities `MapQuestEntity`, `QuestStepEntity`, and `PlayerEntity`.
 * It provides DAOs (`MapQuestDao` and `PlayerDao`) to manage quests, steps, and player data, enabling clean and structured access to queries, updates, and transactions.
 */

@Database(
    entities = [
        AdventureEntity::class,
        QuestEntity::class,
        HintEntity::class,
        PlayerEntity::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mapQuestDao(): AdventureDao
    abstract fun playerDao(): PlayerDao
}