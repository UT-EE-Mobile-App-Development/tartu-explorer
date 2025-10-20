package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.*

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
