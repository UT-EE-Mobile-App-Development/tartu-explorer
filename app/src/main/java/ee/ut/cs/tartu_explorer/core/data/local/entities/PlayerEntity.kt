package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")
data class PlayerEntity(
    @PrimaryKey val id: Int = 1,  // Standardmäßig nur ein Spieler
    val name: String,
    val completedQuests: Int = 0,
    val totalSteps: Int = 0,
    val hintsUsed: Int = 0,
    val experiencePoints: Int = 0
)
