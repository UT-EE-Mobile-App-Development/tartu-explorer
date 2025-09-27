package ee.ut.cs.tartu_explorer.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ee.ut.cs.tartu_explorer.core.data.dao.HintDao
import ee.ut.cs.tartu_explorer.core.data.dao.MissionDao
import ee.ut.cs.tartu_explorer.core.data.dao.PlaytroughDao
import ee.ut.cs.tartu_explorer.core.data.dao.QuestDao
import ee.ut.cs.tartu_explorer.core.data.entity.Hint
import ee.ut.cs.tartu_explorer.core.data.entity.Mission
import ee.ut.cs.tartu_explorer.core.data.entity.Playtrough
import ee.ut.cs.tartu_explorer.core.data.entity.Quest

@Database(
    entities = [Hint::class, Quest::class, Mission::class, Playtrough::class],
    version = 1
)
abstract class TartuExplorerDatabase: RoomDatabase() {
    abstract fun hintDao(): HintDao
    abstract fun missionDao(): MissionDao
    abstract fun questDao(): QuestDao
    abstract fun playtroughDao(): PlaytroughDao
}