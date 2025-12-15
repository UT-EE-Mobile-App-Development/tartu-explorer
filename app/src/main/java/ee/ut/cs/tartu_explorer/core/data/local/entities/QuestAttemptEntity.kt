package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.*

/**
 * Entity representing an attempt made by a player to complete a quest
 * within a specific adventure session.
 *
 * @property id Unique identifier for the quest attempt.
 * @property sessionId Identifier of the adventure session in which the attempt was made.
 * @property questId Identifier of the quest that was attempted.
 * @property wasCorrect Boolean indicating whether the attempt was correct.
 * @property timestamp Timestamp of when the attempt was made.
 */
@Entity(
    tableName = "quest_attempt",
    foreignKeys = [
        ForeignKey(entity = AdventureSessionEntity::class, parentColumns = ["id"], childColumns = ["sessionId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = QuestEntity::class, parentColumns = ["id"], childColumns = ["questId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index(value = ["sessionId"]), Index(value = ["questId"]), Index(value = ["sessionId","questId"])]
)
data class QuestAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: Long,
    val questId: Long,
    val wasCorrect: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
