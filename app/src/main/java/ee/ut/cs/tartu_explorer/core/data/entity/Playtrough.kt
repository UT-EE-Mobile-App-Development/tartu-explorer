package ee.ut.cs.tartu_explorer.core.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Mission::class,
        parentColumns = ["id"],
        childColumns = ["missionId"],
        onDelete = CASCADE
    )],
    indices = [Index(value = ["missionId"])]
)
data class Playtrough(
    val active: Boolean,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val startedAt: String,
    val missionId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
