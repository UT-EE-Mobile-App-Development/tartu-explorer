package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.*

/**
 * Represents a single adventure session.
 *
 * Links:
 * - `playerId` → {PlayerEntity}
 * - `adventureId` → {AdventureEntity}
 *
 * Tracks:
 * - `startedAt`, `completedAt` → timestamps
 * - `status` → session status
 */
@Entity(
    tableName = "adventure_session",
    foreignKeys = [
        ForeignKey(entity = PlayerEntity::class, parentColumns = ["id"], childColumns = ["playerId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = AdventureEntity::class, parentColumns = ["id"], childColumns = ["adventureId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index(value = ["playerId"]), Index(value = ["adventureId"]), Index(value = ["playerId","adventureId"])]
)
data class AdventureSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val playerId: Long,
    val adventureId: Long,
    val startedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    @ColumnInfo(defaultValue = "IN_PROGRESS") val status: SessionStatus = SessionStatus.IN_PROGRESS
)

enum class SessionStatus { IN_PROGRESS, COMPLETED, ABANDONED }