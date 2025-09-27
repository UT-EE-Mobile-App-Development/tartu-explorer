package ee.ut.cs.tartu_explorer.core.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    primaryKeys = ["questId", "index"],
    foreignKeys = [ForeignKey(
        entity = Quest::class,
        parentColumns = ["id"],
        childColumns = ["questId"],
        onDelete=CASCADE)]
)
data class Hint(
    val imageUrl: String?,
    val text: String?,
    val questId: Int,
    val index: Int
)