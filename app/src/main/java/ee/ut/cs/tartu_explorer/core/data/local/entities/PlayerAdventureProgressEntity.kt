package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.*

/**
 * Represents a player's progress in a specific adventure.
 *
 * Tracks:
 * - `playerId`, `adventureId` → player and adventure references
 * - `currentQuestId` → current quest (optional)
 * - `updatedAt` → last progress update
 *
 * Constraints:
 * - Unique combination of `playerId` and `adventureId`
 *
 * Purpose:
 * - Tracks and resumes player progress across adventures
 * - Enables quick lookups and data integrity
 */

@Entity(
    tableName = "player_adventure_progress",
    indices = [Index(value = ["playerId","adventureId"], unique = true)]
)
data class PlayerAdventureProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val playerId: Long,
    val adventureId: Long,
    val currentQuestId: Long?,
    val updatedAt: Long = System.currentTimeMillis()
)