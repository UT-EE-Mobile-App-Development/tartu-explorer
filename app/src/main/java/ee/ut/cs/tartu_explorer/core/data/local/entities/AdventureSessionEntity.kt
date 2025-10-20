package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "adventure_session",
    foreignKeys = [
        ForeignKey(
            entity = AdventureEntity::class,
            parentColumns = ["id"],
            childColumns = ["adventureId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = ["id"],
            childColumns = ["playerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["adventureId"]), Index(value = ["playerId"])]
)
data class AdventureSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val adventureId: Long,
    val playerId: Long,
    val startTime: Long = System.currentTimeMillis(),
    var endTime: Long? = null,
    var status: SessionStatus = SessionStatus.IN_PROGRESS,
    var currentQuestIndex: Int = 0,
    var currentHintIndex: Int = 0
)
