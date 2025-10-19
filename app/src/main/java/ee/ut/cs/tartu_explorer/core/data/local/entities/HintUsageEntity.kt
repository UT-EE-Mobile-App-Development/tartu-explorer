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
            entity = AdventureEntity::class,
            parentColumns = ["id"],
            childColumns = ["adventureId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = HintEntity::class,
            parentColumns = ["id"],
            childColumns = ["hintId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["adventureId"]),
        Index(value = ["hintId"])
    ]
)

data class HintUsageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val adventureId: Long,
    val hintId: Long,
    val usedAt: Long = System.currentTimeMillis()
)
