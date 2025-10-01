package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a map quest (Adventure) in the local database.
 *
 * Defines the structure of a quest with its core attributes:
 * @property id Auto-generated primary key.
 * @property title Quest title.
 * @property description Detailed quest description or objectives.
 * @property difficulty Difficulty level as an integer.
 * @property estimatedDuration Estimated completion time in minutes.
 * @property thumbnailPath Path or URL of the quest thumbnail image.
 * @property completed Whether the quest is completed (default: false).
 */


@Entity(tableName = "adventure")
data class AdventureEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val difficulty: AdventureDifficulty,
    val estimatedDuration: Int,
    val thumbnailPath: String,
    val completed: Boolean = false
)

