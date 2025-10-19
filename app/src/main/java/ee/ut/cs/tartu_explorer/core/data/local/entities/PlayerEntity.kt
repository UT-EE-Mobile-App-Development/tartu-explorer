package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a player in the local database.
 *
 * @property id The primary key representing the player's unique identifier. Defaults to 1, assuming a single player scenario.
 * @property name The name of the player.
 * @property completedQuests The total number of quests completed by the player. Defaults to 0.
 * @property totalSteps The cumulative number of steps taken by the player across all quests. Defaults to 0.
 * @property hintsUsed The total number of hints utilized by the player. Defaults to 0.
 * @property experiencePoints The number of experience points earned by the player. Defaults to 0.
 */
@Entity(tableName = "player")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val experiencePoints: Int = 0
)
