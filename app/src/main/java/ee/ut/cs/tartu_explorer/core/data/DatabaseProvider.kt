package ee.ut.cs.tartu_explorer.core.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var INSTANCE: TartuExplorerDatabase? = null

    fun getDatabase(context: Context): TartuExplorerDatabase {
        return INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                TartuExplorerDatabase::class.java,
                "tartuExplorer.db"
            )
                .createFromAsset("exampleData.db")
                .build().also { INSTANCE = it }
        }
    }
}