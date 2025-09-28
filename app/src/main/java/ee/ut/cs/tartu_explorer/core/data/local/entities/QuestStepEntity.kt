package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity representing a step in a quest within the local database.
 *
 * This entity is linked to the `MapQuestEntity` table via a foreign key relationship,
 * indicating which quest the step belongs to. Steps are defined by their message and
 * associated quest ID, enabling ordered and descriptive steps for quests.
 *
 * @property id Auto-generated primary key representing the unique identifier of the quest step.
 * @property questId Foreign key referencing the associated `MapQuestEntity`.
 *                   Defines which quest this step belongs to.
 * @property message Text or description of the step, providing instructions or details
 *                   for the quest participant.
 */
@Entity(
    tableName = "quest_steps",
    foreignKeys = [
        ForeignKey(
            entity = MapQuestEntity::class,
            parentColumns = ["id"],
            childColumns = ["questId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["questId"])]
)
data class QuestStepEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val questId: Long,
    val message: String // Dieses Feld hat gefehlt
)
