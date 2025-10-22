package ee.ut.cs.tartu_explorer.core.data.local.db

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureDifficulty
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.HintEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrepopulateCallback(private val context: Context) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            val database = DatabaseProvider.getDatabase(context)
            val adventureDao = database.adventureDao()
            val questDao = database.questDao()
            val hintDao = database.hintDao()

            // Adventures
            val adventure1Id = adventureDao.insertAdventure(
                AdventureEntity(
                    title = "Test-Adventure (Delta/Raatuse)",
                    description = "Only a testadventure",
                    difficulty = AdventureDifficulty.EASY,
                    estimatedDuration = 60,
                    thumbnailPath = "https://www.campus.ee/wp-content/uploads/2023/04/Raatuse22_uustaust.png"
                )
            )

            val adventure2Id = adventureDao.insertAdventure(
                AdventureEntity(
                    title = "Street-Art-Tour",
                    description = "Find the hidden art gems of Tartu's streets",
                    difficulty = AdventureDifficulty.MEDIUM,
                    estimatedDuration = 90,
                    thumbnailPath = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQXVVqwTP4A3jU9Gmq9bEOGoFW-4DCBAvsRDA&s"
                )
            )

            // Quests for Adventure 1
            val quest1Id = questDao.insert(
                QuestEntity(
                    adventureId = adventure1Id,
                    latitude = 58.385317,
                    longitude = 26.725464,
                    radius = 100.0
                )
            )
            val quest2Id = questDao.insert(
                QuestEntity(
                    adventureId = adventure1Id,
                    latitude = 58.382572,
                    longitude = 26.732026,
                    radius = 50.0
                )
            )

            // Hints for Quest 1
            hintDao.insert(HintEntity(questId = quest1Id, index = 0, text = "Look for the leaning house.", imageUrl = "https://progressarch.com/wp-content/uploads/2024/04/5_DELTA-BUILDING_TARTU-UNIVERSITY_SCORPIO-P04010-scaled.jpg"))
            hintDao.insert(HintEntity(questId = quest1Id, index = 1, text = "It's near the main square.", imageUrl = "https://www.themayor.eu/web/files/articles/4510/main_image/thumb_1200x630_Delta_Building_Tartu_University.jpg"))

            // Hints for Quest 2
            hintDao.insert(HintEntity(questId = quest2Id, index = 0, text = "This building is a famous university building.", imageUrl = "https://www.campus.ee/wp-content/uploads/2023/04/tyk-6628-2048x1363.jpg"))
            hintDao.insert(HintEntity(questId = quest2Id, index = 1, text = "It has six large white columns.", imageUrl= "https://www.campus.ee/wp-content/uploads/2023/04/tyk-6628-2048x1363.jpg" ))

            // Quests for Adventure 2
            val quest3Id = questDao.insert(
                QuestEntity(
                    adventureId = adventure2Id,
                    latitude = 58.3783,
                    longitude = 26.7149,
                    radius = 30.0
                )
            )
            hintDao.insert(HintEntity(questId = quest3Id, index = 0, text = "Find the mural of the man with the camera."))
        }
    }
}
