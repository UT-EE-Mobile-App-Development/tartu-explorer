package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "hint",
    foreignKeys = [ForeignKey(
        entity = QuestEntity::class,
        parentColumns = ["id"],
        childColumns = ["questId"],
        onDelete = CASCADE
    )]
)
data class HintEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val questId: Long,
    val index: Int,
    val text: String? = null,
    val imageUrl: String? = null
)
