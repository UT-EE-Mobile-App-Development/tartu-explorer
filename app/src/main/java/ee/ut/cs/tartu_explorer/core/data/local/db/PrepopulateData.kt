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

            demoQuests(adventureDao, questDao, hintDao)
            riversideWalk(adventureDao, questDao, hintDao)
            outOfTheCity(adventureDao, questDao, hintDao)
            throughTheCenter(adventureDao, questDao, hintDao)
            hillsideAdventure(adventureDao, questDao, hintDao)
            feelingArtsy(adventureDao, questDao, hintDao)
            trainArrival(adventureDao, questDao, hintDao)
            statuesOfTartu(adventureDao, questDao, hintDao)
        }
    }

    suspend fun demoQuests(adventureDao: AdventureDao, questDao: QuestDao, hintDao: HintDao) {
        // Adventures
        val adventure1Id = adventureDao.insertAdventure(
            AdventureEntity(
                title = "Adventure (Delta/Raatuse)",
                description = "A good start to get to know Tartu Explorer",
                difficulty = AdventureDifficulty.VERY_EASY,
                estimatedDuration = 30,
                thumbnailPath = "https://www.campus.ee/wp-content/uploads/2023/04/Raatuse22_uustaust.png"
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
                text = "Do you know the Tartu University?",
                imageUrl = "https://progressarch.com/wp-content/uploads/2024/04/5_DELTA-BUILDING_TARTU-UNIVERSITY_SCORPIO-P04010-scaled.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = quest1Id,
                index = 1,
                text = "Maybe it is a special building of the university with a unique shape.",
                imageUrl = "https://www.themayor.eu/web/files/articles/4510/main_image/thumb_1200x630_Delta_Building_Tartu_University.jpg"
            )
        )

        // Hints for Quest 2
        hintDao.insert(
            HintEntity(
                questId = quest2Id,
                index = 0,
                text = "Students sleep here",
                imageUrl = "https://www.rallyestonia.com/get/image/userfiles/image/list/item_7062/92094377.jpg?w=1000&h=0"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = quest2Id,
                index = 1,
                text = "The Raatuse street is very close to this building",
                imageUrl = "https://www.campus.ee/wp-content/uploads/2023/04/tyk-6628-2048x1363.jpg"
            )
        )
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
                text = "Look for colorful street art near the water.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/2/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q2,
                index = 2,
                text = "This art isn't in a gallery; it's hiding under a large structure.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/2/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q2,
                index = 3,
                text = "Find the bridge closest to the Delta Centre and look underneath.",
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
                text = "A thoughtful figure rests permanently in a park.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/31/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q31,
                index = 2,
                text = "This bronze man is sitting on a bench, looking towards the river.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/31/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q31,
                index = 3,
                text = "He's a famous surgeon (Pirogov) enjoying the view near the 'Kaarsild' (Arch Bridge).",
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
                text = "Everything is so calm around this statue.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/33/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q33,
                index = 2,
                text = "A young man sits area near the Emajõgi.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/33/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q33,
                index = 3,
                text = "This statue is located in the park near the Town Hall Square and the river.",
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
                text = "A famous Estonian writer (Oskar Luts)",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/51/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q51,
                index = 2,
                text = "This street is big and has a lot of traffic. It is captured in bronze near the Vabaduse puiestee.",
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
                text = "Find this famous statue in a park near the river",
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
                text = "A wall covered in many different faces, crafted from tiles.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/3/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q3,
                index = 2,
                text = "It is close to a large bridge next to a modern University building.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/3/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q3,
                index = 3,
                text = "Famous graffiti on the other side of the river opposite the delta",
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
                text = "A large house made of glass.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/17/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q17,
                index = 2,
                text = "This is the main greenhouse in a large public garden.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/17/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q17,
                index = 3,
                text = "You'll find this palm house inside the University of Tartu Botanical Garden. You are able to claim this quest from the entry when the garden is closed",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/17/3.jpg"
            )
        )
        /*
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
         */
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
                text = "A modern building where university students get active.",
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
                text = "Everything is so calm around this statue.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/33/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q33,
                index = 2,
                text = "A young man sits area near the Emajõgi.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/33/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q33,
                index = 3,
                text = "This statue is located in the park near the Town Hall Square and the river.",
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
                text = "A wonderful place to relax near the river.",
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
                text = "a place in the center of Tartu near the Emajõgi.",
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
                text = "One of the largest bridges in Tartu with many lanterns",
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
                text = "You'll find this pig guarding the entrance to the main Market Hall.",
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
                text = "A beloved storyteller sits permanently in the park along Freedom Avenue.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/29/1.jpg"
            )
        )

        // q50
        val q50 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.378917,
                longitude = 26.724556,
                radius = 15.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q50,
                index = 1,
                text = "This bronze statue is next to a park",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/50/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q50,
                index = 2,
                text = " The Sculpture Father and Son is in the center of Tartu a street of fresh food.",
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
                text = "A massive, colorful story painted on the side of a building.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/4/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q4,
                index = 2,
                text = "This fairytale-like mural is near a big mall.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/4/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q4,
                index = 3,
                text = "It is also not far away from the old town",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/4/3.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q4,
                index = 4,
                text = "Look for the painting between the mal center and the old town.",
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
                text = "A modern apartment building.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/41/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q41,
                index = 2,
                text = "This is not far away from the malls in the center of tartu",
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
                text = "This residential building is located on a street corner near a major commercial area.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/40/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q40,
                index = 2,
                text = "You can find this apartment block on Aleksandri Street, close to the city's main market.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/40/2.jpg"
            )
        )
    }

    //optional: insert more hint texts
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
                text = "A big park close to the old town of Tartu",
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
                text = "A beloved storyteller sits permanently in the park along Freedom Avenue.",
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
                text = "A famous fountain celebrating young love.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/34/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q34,
                index = 2,
                text = "This is the most famous meeting point in Tartu, right in the main square.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/34/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q34,
                index = 3,
                text = "Find the 'Kissing Students' fountain in the center of the Town Hall Square (Raekoja plats).",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/34/3.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q34,
                index = 4,
                text = "It's right in front of the pink Town Hall building!",
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
                text = "Is this the University of Tartu?.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/44/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q44,
                index = 2,
                text = "A large painting of the main building of the University of Tartu.",
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
                text = "A unique restaurant.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/43/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q43,
                index = 2,
                text = "This old 'Gunpowder Cellar' (Püssirohukelder) is now a popular pub at the base of Toomemägi (Toome Hill). Its built into the side of a hill",
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
                text = "The most important academic building in Tartu, known for its grand columns.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/45/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q45,
                index = 2,
                text = "This is the main building of the University of Tartu (Ülikooli peahoone).",
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
                text = "A solemn memorial dedicated to a key figure in Estonian independence.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/47/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q46,
                index = 2,
                text = "Find this granite column with a portrait plaque near the University Library on Lossi Street.",
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
                text = "You are near an old brick building in a calm part of the city.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/47/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q47,
                index = 2,
                text = "Look for a warm-colored building slightly set back from the street.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/47/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q47,
                index = 3,
                text = "Head toward Munga Street, the lively party area, and look for a long building with distinct windows nearby.",
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
                text = "An old, well-known brick building.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/14/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q14,
                index = 2,
                text = "One of the remnants of the Tartu city wall.",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/14/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q14,
                index = 3,
                text = "Not far from the botanical garden.",
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
                text = "A well-known pedestrian bridge near the old town",
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/49/1.jpg"
            )
        )
    }

    suspend fun hillsideAdventure(adventureDao: AdventureDao, questDao: QuestDao, hintDao: HintDao) {
        val adventure = adventureDao.insertAdventure(
            AdventureEntity(
                title = "Hillside Adventure",
                description = "Explore the area around the hills of the observatorium",
                difficulty = AdventureDifficulty.MEDIUM,
                estimatedDuration = 30,
                thumbnailPath = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/25/3.jpg"
            )
        )

        // q25
        val q25 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.379048056599274,
                longitude = 26.719748335391483,
                radius = 40.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q25,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/25/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q25,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/25/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q25,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/25/3.jpg"
            )
        )

        // q23
        val q23 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.379606233106415,
                longitude = 26.71770054624907,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q23,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/23/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q23,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/23/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q23,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/23/3.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q23,
                index = 4,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/23/4.jpg"
            )
        )

        // q19 - building ruins
        val q19 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.380271875299535,
                longitude = 26.714821534639743,
                radius = 50.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q19,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/19/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q19,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/19/2.jpg"
            )
        )

        // q35
        val q35 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.380729,
                longitude = 26.714635,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q35,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/35/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q35,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/35/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q35,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/35/3.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q35,
                index = 4,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/35/4.jpg"
            )
        )

        // q36
        val q36 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.380856045503755,
                longitude = 26.715959021996326,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q36,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/36/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q36,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/36/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q36,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/36/3.jpg"
            )
        )

        // q37
        val q37 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.380930164923136,
                longitude = 26.71469489908313,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q37,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/37/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q37,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/37/2.jpg"
            )
        )

        // q18
        val q18 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.38112541663602,
                longitude = 26.714028981345724,
                radius = 25.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q18,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/18/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q18,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/18/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q18,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/18/3.jpg"
            )
        )
    }

    suspend fun feelingArtsy(adventureDao: AdventureDao, questDao: QuestDao, hintDao: HintDao) {

        val adventure = adventureDao.insertAdventure(
            AdventureEntity(
                title = "Feeling Artsy",
                description = "Take some time to explore some fancy buildings near Tartu's art city",
                difficulty = AdventureDifficulty.MEDIUM,
                estimatedDuration = 30,
                thumbnailPath = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/12/3.jpg"
            )
        )

        // q7
        val q7 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.369846,
                longitude = 26.711979,
                radius = 40.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q7,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/7/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q7,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/7/2.jpg"
            )
        )

        // q11
        val q11 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.36990572719961,
                longitude = 26.71248473091544,
                radius = 50.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q11,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/11/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q11,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/11/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q11,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/11/3.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q11,
                index = 4,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/11/4.jpg"
            )
        )

        // q8
        val q8 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.370609,
                longitude = 26.714893,
                radius = 25.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q8,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/8/1.jpg"
            )
        )

        // q12
        val q12 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.37053280119555,
                longitude = 26.716004661558966,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q12,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/12/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q12,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/12/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q12,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/12/3.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q12,
                index = 4,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/12/4.jpg"
            )
        )

        // q13
        val q13 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.37158866094387,
                longitude = 26.716051556723635,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q13,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/13/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q13,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/23/1.jpg"
            )
        )

        // q26
        val q26 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.373790,
                longitude = 26.719644,
                radius = 40.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q26,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/26/1.jpg"
            )
        )

        // q1
        val q1 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.376605493385775,
                longitude = 26.722048602961475,
                radius = 25.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q1,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/1/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q1,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/1/2.jpg"
            )
        )

    }

    suspend fun trainArrival(adventureDao: AdventureDao, questDao: QuestDao, hintDao: HintDao) {

        val adventure = adventureDao.insertAdventure(
            AdventureEntity(
                title = "Train Arrival",
                description = "You arrived in Tartu by Train and make your journey towards the city centre",
                difficulty = AdventureDifficulty.HARD,
                estimatedDuration = 40,
                thumbnailPath = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/27/1.jpg"
            )
        )

        // q38
        val q38 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.37550699457386,
                longitude = 26.708628306030366,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q38,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/38/1.jpg"
            )
        )

        // q9
        val q9 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.376083,
                longitude = 26.710954,
                radius = 25.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q9,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/9/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q9,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/9/2.jpg"
            )
        )

        // q10
        val q10 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.376148,
                longitude = 26.717446,
                radius = 25.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q10,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/10/1.jpg"
            )
        )

        // q39
        val q39 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.376470,
                longitude = 26.713218,
                radius = 25.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q39,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/39/1.jpg"
            )
        )

        // q27
        val q27 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.37666843152637,
                longitude = 26.716379092322295,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q27,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/27/1.jpg"
            )
        )

        // q6
        val q6 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.378607,
                longitude = 26.715297,
                radius = 25.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q6,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/6/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q6,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/art/6/2.jpg"
            )
        )

        // q24
        val q24 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.37822876405451,
                longitude = 58.37822876405451,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q24,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/24/1.jpg"
            )
        )

        // q23
        val q23 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.379606233106415,
                longitude = 26.71770054624907,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q23,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/23/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q23,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/23/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q23,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/23/3.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q23,
                index = 4,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/buildings/23/4.jpg"
            )
        )

        // q44
        val q44 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.38029785645126 ,
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

    }

    suspend fun statuesOfTartu(adventureDao: AdventureDao, questDao: QuestDao, hintDao: HintDao) {
        val adventure = adventureDao.insertAdventure(
            AdventureEntity(
                title = "Statues of Tartu",
                description = "Explore some of the many statues in Tartu",
                difficulty = AdventureDifficulty.VERY_HARD,
                estimatedDuration = 90,
                thumbnailPath = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/32/1.jpg"
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

        // q32 3 (center hesburger)
        val q32 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude =58.38058535021522,
                longitude = 26.724478704379546,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q32,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/32/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q32,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/32/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q32,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/32/3.jpg"
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

        // q30
        val q30 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.378212538822204,
                longitude = 26.722976010770186,
                radius = 20.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q30,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/30/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q30,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/30/2.jpg"
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

        // q35
        val q35 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.380729,
                longitude = 26.714635,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q35,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/35/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q35,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/35/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q35,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/35/3.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q35,
                index = 4,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/35/4.jpg"
            )
        )

        // q36
        val q36 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.380856045503755,
                longitude = 26.715959021996326,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q36,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/36/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q36,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/36/2.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q36,
                index = 3,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/36/3.jpg"
            )
        )

        // q37
        val q37 = questDao.insert(
            QuestEntity(
                adventureId = adventure,
                latitude = 58.380930164923136,
                longitude = 26.71469489908313,
                radius = 30.0
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q37,
                index = 1,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/37/1.jpg"
            )
        )
        hintDao.insert(
            HintEntity(
                questId = q37,
                index = 2,
                text = null,
                imageUrl = "https://raw.githubusercontent.com/UT-EE-Mobile-App-Development/tartu-explorer-quests/refs/heads/main/statues/37/2.jpg"
            )
        )

    }
}
