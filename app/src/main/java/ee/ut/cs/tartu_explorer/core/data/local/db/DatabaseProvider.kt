package ee.ut.cs.tartu_explorer.core.data.local.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureDifficulty
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null
    private const val FORCE_RECREATE = true  // false to keep the data in the database

    fun getDatabase(context: Context): AppDatabase {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        // Later to change over shard preferences, from Settings-Menü or Debug-Screen
        var forceRecreate = prefs.getBoolean("force_recreate_db", false)
        forceRecreate = FORCE_RECREATE

        // later from someware in the code:
        // val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        // prefs.edit().putBoolean("force_recreate_db", true).apply()

        if (forceRecreate) {
            context.deleteDatabase("tartuExplorer.db")
            prefs.edit().putBoolean("force_recreate_db", false).apply()
        }


        return INSTANCE ?: synchronized(this) {
            val instance = androidx.room.Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "tartuExplorer.db"
            )
                .fallbackToDestructiveMigration()
                .addCallback(PrepopulateCallback(context))
                .build()
            INSTANCE = instance
            instance
        }
    }
}


