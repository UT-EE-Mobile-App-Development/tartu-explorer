package ee.ut.cs.tartu_explorer.core.data.local.db

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    // Keys for SharedPreferences to be used by the Dev Panel
    private const val PREFS_NAME = "app_prefs"
    const val KEY_FORCE_RECREATE_ONCE = "force_recreate_db"
    const val KEY_RESET_ON_START = "reset_on_start"

    fun getDatabase(context: Context): AppDatabase {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var forceRecreateOnce = prefs.getBoolean(KEY_FORCE_RECREATE_ONCE, false)
        val resetOnEveryStart = prefs.getBoolean(KEY_RESET_ON_START, false)

        if (forceRecreateOnce || resetOnEveryStart) {
            // If any flag is set, delete the database.
            context.deleteDatabase("tartuExplorer.db")
            INSTANCE = null // Crucial: Nullify the instance to force re-creation.

            // If this was a one-time action, reset the flag immediately.
            if (forceRecreateOnce) {
                prefs.edit().putBoolean(KEY_FORCE_RECREATE_ONCE, false).apply()
            }
        }

        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "tartuExplorer.db"
            )
                .fallbackToDestructiveMigration()
                .addCallback(PrepopulateCallback(context))
                .build().also { INSTANCE = it }
        }
    }
}
