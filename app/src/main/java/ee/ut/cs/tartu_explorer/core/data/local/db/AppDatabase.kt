package ee.ut.cs.tartu_explorer.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ee.ut.cs.tartu_explorer.core.data.local.dao.AdventureDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.HintDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.PlayerDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.QuestDao
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestEntity

/**
 * AppDatabase is the Room database for the app, containing all the entities.
 * It provides DAOs to manage quests, steps, and player data, enabling clean and structured access to queries, updates, and transactions.
 */

@Database(
    entities = [
        AdventureEntity::class,
        QuestEntity::class,
        HintEntity::class,
        PlayerEntity::class,
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun adventureDao(): AdventureDao
    abstract fun questDao(): QuestDao
    abstract fun hintDao(): HintDao
    abstract fun playerDao(): PlayerDao
}