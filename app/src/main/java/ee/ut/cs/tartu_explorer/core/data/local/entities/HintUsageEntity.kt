package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.*

/**
 * Represents the use of a hint during a quest attempt.
 *
 * Tracks:
 * - `sessionId`, `attemptId` → related session and attempt
 * - `hintQuestId`, `hintIndex` → optional hint reference
 *
 * Indexed by:
 * - `sessionId`, `attemptId`, and (`hintQuestId`, `hintIndex`)
 *
 * Deletion:
 * - Cascade on session and attempt removal
 * - Set hint fields to null if hint is deleted
 */

@Entity(
    tableName = "hint_usage",
    foreignKeys = [
        ForeignKey(
            entity = AdventureSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = QuestAttemptEntity::class,
            parentColumns = ["id"],
            childColumns = ["attemptId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = HintEntity::class,
            parentColumns = ["questId", "index"],
            childColumns = ["hintQuestId", "hintIndex"],
            onDelete = ForeignKey.SET_NULL
        ),
    ],
    indices = [
        Index(value = ["sessionId"]),
        Index(value = ["attemptId"]),
        Index(value = ["hintQuestId", "hintIndex"])
    ]
)
data class HintUsageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: Long,
    val attemptId: Long,
    val hintQuestId: Int?,
    val hintIndex: Int?,
    val usedAt: Long = System.currentTimeMillis()
)