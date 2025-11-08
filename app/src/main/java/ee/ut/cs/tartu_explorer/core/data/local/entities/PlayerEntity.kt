package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a player in the local database.
 *
 * @property id The primary key representing the player's unique identifier. Is 0 by default and will be auto-generated.
 * @property name The name of the player.
 * @property experiencePoints The number of experience points earned by the player. Defaults to 0.
 * @property isActive Boolean flag to determine the current active player. Defaults to false.
 */
@Entity(tableName = "player")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val experiencePoints: Int = 0,
    val isActive: Boolean = false
)
