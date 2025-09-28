package ee.ut.cs.tartu_explorer.core.data.local.db

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "tartuExplorer.db"
            )
                .createFromAsset("exampleData.db")
                .fallbackToDestructiveMigration(true)
                .build().also { INSTANCE = it }
        }
    }
}