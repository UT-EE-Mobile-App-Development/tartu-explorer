package ee.ut.cs.tartu_explorer.feature.testing

import ee.ut.cs.tartu_explorer.core.data.local.entities.MapQuestEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.QuestStepEntity

object TestDataGenerator {

    fun getTestPlayer(): PlayerEntity {
        return PlayerEntity(
            id = 1,
            name = "Test Player",
            experiencePoints = 100
        )
    }

    fun getTestQuests(): List<MapQuestEntity> {
        return listOf(
            MapQuestEntity(
                id = 1,
                title = "Entdecke die Altstadt",
                description = "Ein Spaziergang durch das historische Herz von Tartu.",
                difficulty = 2,
                estimatedDuration = 60,
                thumbnailPath = "path/to/thumbnail1.jpg"
            ),
            MapQuestEntity(
                id = 2,
                title = "Street-Art-Tour",
                description = "Finde die versteckten Kunstwerke in den Straßen von Tartu.",
                difficulty = 3,
                estimatedDuration = 90,
                thumbnailPath = "path/to/thumbnail2.jpg"
            )
        )
    }

    fun getTestQuestSteps(): List<QuestStepEntity> {
        return listOf(
            // Schritte für Quest 1
            QuestStepEntity(questId = 1, message = "Finde den Rathausplatz."),
            QuestStepEntity(questId = 1, message = "Mache ein Foto von der Skulptur 'Die küssenden Studenten'."),
            QuestStepEntity(questId = 1, message = "Besuche die Johanniskirche."),

            // Schritte für Quest 2
            QuestStepEntity(questId = 2, message = "Suche das große Wandgemälde in der Nähe der Universität."),
            QuestStepEntity(questId = 2, message = "Finde das Graffiti mit dem Titel 'Freiheit'."),
            QuestStepEntity(questId = 2, message = "Entdecke die kleine Maus-Statue.")
        )
    }
}