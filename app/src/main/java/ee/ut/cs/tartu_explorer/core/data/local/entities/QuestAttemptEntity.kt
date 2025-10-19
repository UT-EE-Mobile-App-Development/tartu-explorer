package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.*

/**
 * Represents a single quest attempt within an adventure session.
 *
 * Links:
 * - `sessionId` → {AdventureSessionEntity}
 * - `questId` → {QuestEntity}
 *
 * Tracks:
 * - `startedAt`, `completedAt` → timestamps
 * - `succeeded` → success status
 * - `attemptIndex` → retry count
 *
 * Indexed by `sessionId`, `questId`, and their combination.
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
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: Long,
    val questId: Long,
    val startedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val succeeded: Boolean = false,
    val attemptIndex: Int = 1
)