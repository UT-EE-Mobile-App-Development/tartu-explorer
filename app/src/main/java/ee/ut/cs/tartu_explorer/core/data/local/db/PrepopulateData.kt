package ee.ut.cs.tartu_explorer.core.data.local.db

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ee.ut.cs.tartu_explorer.core.data.local.dao.AdventureDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.HintDao
import ee.ut.cs.tartu_explorer.core.data.local.dao.QuestDao
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
            hintDao.insert(
                HintEntity(
                    questId = quest1Id,
                    index = 0,
                    text = "Look for the leaning house.",
                    imageUrl = "https://progressarch.com/wp-content/uploads/2024/04/5_DELTA-BUILDING_TARTU-UNIVERSITY_SCORPIO-P04010-scaled.jpg"
                )
            )
            hintDao.insert(
                HintEntity(
                    questId = quest1Id,
                    index = 1,
                    text = "It's near the main square.",
                    imageUrl = "https://www.themayor.eu/web/files/articles/4510/main_image/thumb_1200x630_Delta_Building_Tartu_University.jpg"
                )
            )

            // Hints for Quest 2
            hintDao.insert(
                HintEntity(
                    questId = quest2Id,
                    index = 0,
                    text = "This building is a famous university building.",
                    imageUrl = "https://www.campus.ee/wp-content/uploads/2023/04/tyk-6628-2048x1363.jpg"
                )
            )
            hintDao.insert(
                HintEntity(
                    questId = quest2Id,
                    index = 1,
                    text = "It has six large white columns.",
                    imageUrl = "https://www.campus.ee/wp-content/uploads/2023/04/tyk-6628-2048x1363.jpg"
                )
            )

            // Quests for Adventure 2
            val quest3Id = questDao.insert(
                QuestEntity(
                    adventureId = adventure2Id,
                    latitude = 58.3783,
                    longitude = 26.7149,
                    radius = 30.0
                )
            )
            hintDao.insert(
                HintEntity(
                    questId = quest3Id,
                    index = 0,
                    text = "Find the mural of the man with the camera."
                )
            )

            riversideWalk(adventureDao, questDao, hintDao)
            outOfTheCity(adventureDao, questDao, hintDao)
            throughTheCenter(adventureDao, questDao, hintDao)
        }
    }

    suspend fun riversideWalk(adventureDao: AdventureDao, questDao: QuestDao, hintDao: HintDao) {

        val adventure = adventureDao.insertAdventure(
            AdventureEntity(
                title = "Riverside-Walk",
                description = "You start at Delta and explore your surrounding along a short walk next to the river",
                difficulty = AdventureDifficulty.EASY,
                estimatedDuration = 30,
                thumbnailPath = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/3/2.jpg"
            )
        )

        // q2
        val q2 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.38435163851936,
                longitude = 26.725568230146422,
                radius = 25.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q2,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/2/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q2,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/2/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q2,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/2/3.jpg"
            )
        )

        // q31
        val q31 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.38193823012518,
                longitude = 26.72667950465788,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q31,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/31/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q31,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/31/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q31,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/31/3.jpg"
            )
        )

        // q33
        val q33 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.38106095384904,
                longitude = 26.726507555533956,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q33,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/33/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q33,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/33/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q33,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/33/3.jpg"
            )
        )

        // q51
        val q51 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.37838931671423,
                longitude = 26.725439477841697,
                radius = 15.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q51,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/51/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q51,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/51/2.jpg"
            )
        )

        // q48
        val q48 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.38261151781606,
                longitude = 26.724528758160066,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q48,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/48/1.jpg"
            )
        )

        // q3
        val q3 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.384384766253426,
                longitude = 26.723514143961868,
                radius = 25.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q3,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/3/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q3,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/3/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q3,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/3/3.jpg"
            )
        )

        // q17
        val q17 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.38406162421939,
                longitude = 26.721182745379824,
                radius = 50.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q17,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/17/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q17,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/17/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q17,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/17/3.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q17,
                index = 4,
                text = "This location is inside the botanical garden",
                imageUrl = null
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q17,
                index = 5,
                text = "You are able to claim this quest from the entry when the garden is closed",
                imageUrl = null
            )
        )

        // q15
        val q15 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.38572061953647,
                longitude = 26.724920101246,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q15,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/15/1.jpg"
            )
        )

    }

    suspend fun outOfTheCity(adventureDao: AdventureDao, questDao: QuestDao, hintDao: HintDao) {

        val adventure = adventureDao.insertAdventure(
            AdventureEntity(
                title = "Out of the city",
                description = "You start along the river and explore parts of the city while moving south",
                difficulty = AdventureDifficulty.MEDIUM,
                estimatedDuration = 40,
                thumbnailPath = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/4/4.jpg"
            )
        )

        // q33
        val q33 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.38106095384904,
                longitude = 26.726507555533956,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q33,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/33/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q33,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/33/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q33,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/33/3.jpg"
            )
        )

        // q20
        val q20 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.380701048618015,
                longitude = 26.726614876877665,
                radius = 20.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q20,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/20/1.jpg"
            )
        )

        // q21
        val q21 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.37972559257077,
                longitude = 26.728517079275726,
                radius = 25.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q21,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/21/1.jpg"
            )
        )

        // q22
        val q22 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.379315552375644,
                longitude = 26.730316202735054,
                radius = 40.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q22,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/22/1.jpg"
            )
        )

        // q28
        val q28 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.378805789574045,
                longitude = 26.728213325917068,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q28,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/28/1.jpg"
            )
        )

        // q29
        val q29 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.378917685592015,
                longitude = 26.7232594018626,
                radius = 25.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q29,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/29/1.jpg"
            )
        )

        // q50
        val q50 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.37838931671423,
                longitude = 26.725439477841697,
                radius = 15.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q50,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/50/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q50,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/50/2.jpg"
            )
        )

        // q4
        val q4 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.3779106864374,
                longitude = 26.726102766515115,
                radius = 25.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q4,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/4/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q4,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/4/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q4,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/4/3.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q4,
                index = 4,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/4/4.jpg"
            )
        )

        // q41
        val q41 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.376515322074866,
                longitude = 26.728329182363037,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q41,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/41/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q41,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/41/2.jpg"
            )
        )

        // q40
        val q40 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.37552538511389,
                longitude = 26.73013992309486,
                radius = 40.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q40,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/40/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q40,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/40/2.jpg"
            )
        )
    }

    suspend fun throughTheCenter(adventureDao: AdventureDao, questDao: QuestDao, hintDao: HintDao) {

        val adventure = adventureDao.insertAdventure(
            AdventureEntity(
                title = "Through the center",
                description = "Explore the city's core trough a new lens",
                difficulty = AdventureDifficulty.MEDIUM,
                estimatedDuration = 30,
                thumbnailPath = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/42/1.jpg"
            )
        )

        // q42
        val q42 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.37913345100393,
                longitude = 26.72394079172503,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q42,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/42/1.jpg"
            )
        )

        // q29
        val q29 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.378917685592015,
                longitude = 26.7232594018626,
                radius = 25.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q29,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/29/1.jpg"
            )
        )

        // q34
        val q34 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.38008564470492,
                longitude = 26.72232177647657,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q34,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/34/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q34,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/34/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q34,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/34/3.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q34,
                index = 4,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/34/4.jpg"
            )
        )

        // q44
        val q44 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.38029785645126,
                longitude = 26.720703357037966,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q44,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/44/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q44,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/44/2.jpg"
            )
        )

        // q43
        val q43 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.37947734094728,
                longitude = 26.71927063002511,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q43,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/43/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q43,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/43/2.jpg"
            )
        )

        // q45
        val q45 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.38114089499359,
                longitude = 26.72029364799623,
                radius = 40.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q45,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/45/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q45,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/45/2.jpg"
            )
        )

        // q46
        val q46 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.38171877581215,
                longitude = 26.720542673885173,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q46,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/47/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q46,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/47/2.jpg"
            )
        )

        // q47
        val q47 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.382138030291706,
                longitude = 26.7213292297671,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q47,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/47/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q47,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/47/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q47,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/47/3.jpg"
            )
        )

        // q14
        val q14 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.383879245746336,
                longitude = 26.721748321932548,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q14,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/14/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q14,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/14/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q14,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/14/3.jpg"
            )
        )

        // q49
        val q49 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.38078924868807,
                longitude = 26.72591147856463,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q49,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/49/1.jpg"
            )
        )
    }
}
