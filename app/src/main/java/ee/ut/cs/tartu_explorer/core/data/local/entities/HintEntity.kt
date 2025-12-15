package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

/**
 * Represents a hint associated with a quest in the local database.
 *
 * @property id The unique identifier for the hint.
 * @property questId The identifier of the quest this hint is associated with.
 * @property index The index of the hint within the quest.
 * @property text The textual content of the hint (optional).
 * @property imageUrl The URL of an image associated with the hint (optional).
 */
@Entity(
    tableName = "hint",
    foreignKeys = [ForeignKey(
        entity = QuestEntity::class,
        parentColumns = ["id"],
        childColumns = ["questId"],
        onDelete = CASCADE
    )]
)
data class HintEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val questId: Long,
    val index: Int,
    val text: String? = null,
    val imageUrl: String? = null
)
