package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity representing a step in a quest within the local database.
 *
 * This entity is linked to the `AdventureEntity` table via a foreign key relationship,
 * indicating which quest the step belongs to. Steps are defined by their message and
 * associated quest ID, enabling ordered and descriptive steps for quests.
 *
 * @property id Auto-generated primary key representing the unique identifier of the quest step.
 * @property adventureId Foreign key referencing the associated `AdventureEntity`.
 *                   Defines which quest this step belongs to.
 * @property latitude Latitude of target point
 * @property longitude Longitude of target point
 * @property radius Radius in metres around the target point that count as being on target.
 */
@Entity(
    tableName = "quest",
    foreignKeys = [
        ForeignKey(
            entity = AdventureEntity::class,
            parentColumns = ["id"],
            childColumns = ["adventureId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["adventureId"])]
)
data class QuestEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val adventureId: Long,
    val latitude: Double,
    val longitude: Double,
    val radius: Double = 25.0
)
