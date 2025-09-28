package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "quest_steps",
    foreignKeys = [
        ForeignKey(
            entity = MapQuestEntity::class,
            parentColumns = ["id"],
            childColumns = ["questId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["questId"])]
)
data class QuestStepEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val questId: Long,
    val message: String // Dieses Feld hat gefehlt
)
