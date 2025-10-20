package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
            entity = QuestEntity::class,
            parentColumns = ["id"],
            childColumns = ["questId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = HintEntity::class,
            parentColumns = ["id"],
            childColumns = ["hintId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = ["id"],
            childColumns = ["playerId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["adventureId"]),
        Index(value = ["questId"]),
        Index(value = ["hintId"]),
        Index(value = ["playerId"])
    ]
)
data class HintUsageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val adventureId: Long,
    val questId: Long,
    val hintId: Long,
    val playerId: Long,
    val usedAt: Long = System.currentTimeMillis()
)
