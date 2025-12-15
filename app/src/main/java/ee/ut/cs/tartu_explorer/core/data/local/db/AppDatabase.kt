package ee.ut.cs.tartu_explorer.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ee.ut.cs.tartu_explorer.core.data.local.dao.AdventureDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.AdventureSessionDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.HintDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.HintUsageDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.PlayerDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.QuestAttemptDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.QuestDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.StatisticsDao
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureDifficultyConverter
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureSessionEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintUsageEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestAttemptEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.SessionStatusConverter

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
        AdventureSessionEntity::class,
        QuestAttemptEntity::class,
        HintUsageEntity::class,
    ],
    version = 7
)

/**
 * Type converters for custom data types used in the database.
 */
@TypeConverters(AdventureDifficultyConverter::class, SessionStatusConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun adventureDao(): AdventureDao
    abstract fun questDao(): QuestDao
    abstract fun hintDao(): HintDao
    abstract fun playerDao(): PlayerDao
    abstract fun statisticsDao(): StatisticsDao
    abstract fun hintUsageDao(): HintUsageDao
    abstract fun adventureSessionDao(): AdventureSessionDao
    abstract fun questAttemptDao(): QuestAttemptDao
}
