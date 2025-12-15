package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents an adventure session entity in the local database.
 *
 * @property id Unique identifier for the adventure session.
 * @property adventureId Foreign key referencing the associated adventure.
 * @property playerId Foreign key referencing the player participating in the session.
 * @property startTime Timestamp marking the start of the session.
 * @property endTime Timestamp marking the end of the session, nullable if the session is ongoing.
 * @property status Current status of the session (e.g., IN_PROGRESS, COMPLETED).
 * @property currentQuestIndex Index of the current quest in the adventure.
 * @property currentHintIndex Index of the current hint in the quest.
 */
@Entity(
    tableName = "adventure_session",
    foreignKeys = [
        ForeignKey(
            entity = AdventureEntity::class,
            parentColumns = ["id"],
            childColumns = ["adventureId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = ["id"],
            childColumns = ["playerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["adventureId"]), Index(value = ["playerId"])]
)
data class AdventureSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val adventureId: Long,
    val playerId: Long,
    val startTime: Long = System.currentTimeMillis(),
    var endTime: Long? = null,
    var status: SessionStatus = SessionStatus.IN_PROGRESS,
    var currentQuestIndex: Int = 0,
    var currentHintIndex: Int = 0
)
