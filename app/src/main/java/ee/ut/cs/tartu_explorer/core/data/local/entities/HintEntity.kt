package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "hint",
    primaryKeys = ["questId", "index"],
    foreignKeys = [ForeignKey(
        entity = QuestStepEntity::class,
        parentColumns = ["id"],
        childColumns = ["questId"],
        onDelete = CASCADE
    )]
)
data class HintEntity(
    val imageUrl: String?,
    val text: String?,
    val questId: Int,
    val index: Int
)