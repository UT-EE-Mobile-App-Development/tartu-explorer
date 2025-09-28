package ee.ut.cs.tartu_explorer.feature.testing

import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureDifficulty
import ee.ut.cs.tartu_explorer.core.data.local.entities.AdventureEntity
import ee.ut.cs.tartu_explorer.core.data.local.entities.PlayerEntity

/**
 * Utility object for generating test data for various entity classes.
 *
 * Provides sample data for `PlayerEntity`, `MapQuestEntity`, and `QuestStepEntity`
 * to facilitate testing and development. All generated data is predefined and fixed,
 * serving as mock data for application components or database operations.
 */
object TestDataGenerator {

    fun getTestPlayer(): PlayerEntity {
        return PlayerEntity(
            id = 1,
            name = "Test Player",
            experiencePoints = 100
        )
    }

    fun getTestQuests(): List<AdventureEntity> {
        return listOf(
            AdventureEntity(
                id = 1,
                title = "Entdecke die Altstadt",
                description = "Ein Spaziergang durch das historische Herz von Tartu.",
                difficulty = AdventureDifficulty.MEDIUM,
                estimatedDuration = 60,
                thumbnailPath = "path/to/thumbnail1.jpg"
            ),
            AdventureEntity(
                id = 2,
                title = "Street-Art-Tour",
                description = "Finde die versteckten Kunstwerke in den Straßen von Tartu.",
                difficulty = AdventureDifficulty.HARD,
                estimatedDuration = 90,
                thumbnailPath = "path/to/thumbnail2.jpg"
            )
        )
    }
}