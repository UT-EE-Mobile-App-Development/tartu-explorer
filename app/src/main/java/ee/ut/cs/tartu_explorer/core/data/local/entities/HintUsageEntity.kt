package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity representing the usage of hints within an adventure session.
 *
 * @property id Unique identifier for the hint usage record.
 * @property sessionId Identifier of the adventure session in which the hint was used.
 * @property hintId Identifier of the hint that was used.
 * @property usedAt Timestamp indicating when the hint was used.
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
            entity = HintEntity::class,
            parentColumns = ["id"],
            childColumns = ["hintId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["sessionId"]), Index(value = ["hintId"])]
)
data class HintUsageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: Long,
    val hintId: Long,
    val usedAt: Long = System.currentTimeMillis()
)
